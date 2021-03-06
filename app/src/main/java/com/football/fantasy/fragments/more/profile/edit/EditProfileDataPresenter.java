package com.football.fantasy.fragments.more.profile.edit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.requests.ProfileRequest;
import com.football.models.responses.UserResponse;
import com.football.utilities.RxUtilities;
import com.football.utilities.compressor.Compressor;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EditProfileDataPresenter extends BaseDataPresenter<IEditProfileView> implements IEditProfilePresenter<IEditProfileView> {
    /**
     * @param appComponent
     */
    protected EditProfileDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getProfile() {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(
                    v,
                    dataModule.getApiService().getProfile(),
                    new ApiCallback<UserResponse>() {
                        @Override
                        public void onStart() {
                            v.showLoading(true);
                        }

                        @Override
                        public void onComplete() {
                            v.showLoading(false);
                        }

                        @Override
                        public void onSuccess(UserResponse response) {
                            v.displayUser(response);
                        }

                        @Override
                        public void onError(String error) {
                            v.showMessage(error);
                        }
                    }));
        });
    }

    @Override
    public void updateProfile(ProfileRequest request) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(
                    v,
                    dataModule.getApiService().updateProfile(createRequest(v.getAppActivity(), request)),
                    new ApiCallback<UserResponse>() {
                        @Override
                        public void onStart() {
                            v.showLoading(true);
                        }

                        @Override
                        public void onComplete() {
                            v.showLoading(false);
                        }

                        @Override
                        public void onSuccess(UserResponse user) {
                            v.updateSuccessful(user);
                        }

                        @Override
                        public void onError(String error) {
                            v.showMessage(error);
                        }
                    }));

        });
    }

    @NonNull
    private MultipartBody createRequest(Context context, ProfileRequest request) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("first_name", request.getFirstName())
                .addFormDataPart("last_name", request.getLastName())
                .addFormDataPart("gender", String.valueOf(request.getGender()))
                .addFormDataPart("address", request.getAddress())
                .addFormDataPart("phone", request.getPhone())
                .addFormDataPart("description", request.getDescription());

        if (!TextUtils.isEmpty(request.getBirthday())) {
            builder.addFormDataPart("birthday", request.getBirthday());
        }

        File photo = request.getPhoto();
        if (photo != null && photo.exists()) {
            File compressFile;
            try {
                compressFile = new Compressor(context.getApplicationContext())
                        .compressToFile(photo);
            } catch (IOException e) {
                compressFile = photo;
                e.printStackTrace();
            }

            builder.addFormDataPart("photo", compressFile.getName(), RequestBody.create(MediaType.parse("image/*"), compressFile));
        }

        return builder.build();
    }

}
