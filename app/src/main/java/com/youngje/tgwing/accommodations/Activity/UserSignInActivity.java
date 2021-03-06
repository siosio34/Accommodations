/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.youngje.tgwing.accommodations.Activity;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.youngje.tgwing.accommodations.Activity.BaseActivity;
import com.youngje.tgwing.accommodations.R;
import com.youngje.tgwing.accommodations.User;
import com.youngje.tgwing.accommodations.Util.LocationUtil;

import java.net.URI;
import java.util.ArrayList;

import static android.R.attr.country;
import static android.R.attr.x;

/**
 * Demonstrate Firebase Authentication using a Google ID Token.
 */
public class UserSignInActivity extends BaseActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private static final int RC_SIGNINFORMATION_IN = 10001;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    // [END declare_auth_listener]

    // Write a message to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("users");

    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private String country;
    private String userDetailInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();

                if (user != null) {
                    checkBasicUser(user);

                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    setContentView(R.layout.activity_user_sign_in);
                    findViewById(R.id.sign_in_button).setOnClickListener(UserSignInActivity.this);

                }

            }
        };

        // [END auth_state_listener]
        setContentView(R.layout.activity_splash);
        // [END auth_state_listener]
        // Button listeners


    }

    // [START on_start_add_listener]
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    // [END on_start_add_listener]

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    // [END on_stop_remove_listener]

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {



                // Google Sign In failed, update UI appropriately
                // [START_EXCLUDE]
                //  updateUI(null);// [END_EXCLUDE]
            }
        }

        else if(requestCode == RC_SIGNINFORMATION_IN) {

            Intent intent = getIntent();
            country = intent.getStringExtra("country");
            userDetailInfo = intent.getStringExtra("detail");
            Log.i("country", country);
            Log.i("detail",userDetailInfo);

        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(UserSignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]

    // [START signin]
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        //    updateUI(null);
                    }
                });
    }

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        //updateUI(null);
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }


    public void checkBasicUser(final FirebaseUser firebaseUser) { //  기존 유저 체크 뒤 정보 불러오기
        System.out.println();
        LocationUtil locationUtil = new LocationUtil();
        final Location curloc = locationUtil.getLocation();

        // 새로 등록할 유저
        final String uid = firebaseUser.getUid();
        Log.i("user information", uid);

        // Read from the database
        myRef.child(uid).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.

                        showProgressDialog();

                        User userValue = dataSnapshot.getValue(User.class);

                        if (userValue == null) {

                           //Intent intent = new Intent(UserSignInActivity.this, MapSearchActivity.class);
                           //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                           //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                           //startActivity(intent);


                            String photoUrl = null;
                            String email = null;
                            String name = null;

                            name = firebaseUser.getDisplayName();
                            email = firebaseUser.getEmail();
                            photoUrl = firebaseUser.getPhotoUrl().toString();
                            User userTemp = new User(uid, name, email, "", photoUrl,country ,"",curloc.getLatitude(),curloc.getLongitude(),new ArrayList<String>());
                            registerUser(uid, userTemp);
                            User.setMyInstance(userTemp);

                            Log.i("신규 유저 정보", User.getMyInstance().toString());

                            // FirebaseDatabase database = FirebaseDatabase.getInstance();
                            // DatabaseReference myRef = database.getReference();
                            // myRef.child("currentUser").child(userTemp.getUserId()).setValue(userTemp);

                            moveToMapSearchActivity();


                            hideProgressDialog();
                            //startActivity(new Intent(getApplicationContext(), WriteReviewActivity.class));
                            //Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                        } else { // 존재할경우 -> 불러와야함

                            User.setMyInstance(userValue);
                            Log.i("기존 유저정보", User.getMyInstance().toString());
                           // FirebaseDatabase database = FirebaseDatabase.getInstance();
                           // DatabaseReference myRef = database.getReference();
                           // myRef.child("currentUser").child(User.getMyInstance().getUserId()).setValue(User.getMyInstance());

                            moveToMapSearchActivity();
                            hideProgressDialog();
                            //startActivity(new Intent(getApplicationContext(), WriteReviewActivity.class));
                            //Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });

    }



    public void registerUser(String uid, User user) { // 새로운 유저 파이어 베이스에 등록
        myRef.child(uid).setValue(user);
    }

    @Override
    public void onClick(View view) {

        int i = view.getId();
        if (i == R.id.sign_in_button) {
            signIn();
        }

    }

    private void moveToMapSearchActivity() {
        Intent intent = new Intent(UserSignInActivity.this, MapSearchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

}