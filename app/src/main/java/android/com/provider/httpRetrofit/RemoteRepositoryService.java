package android.com.provider.httpRetrofit;


import android.com.provider.apiResponses.GetProviderBio;
import android.com.provider.apiResponses.SendBioModel;
import android.com.provider.apiResponses.acceptInstantBookingApi.AcceptInstantBooking;
import android.com.provider.apiResponses.cancelledJobApi.CancelledJob;
import android.com.provider.apiResponses.cancelledJobByProvider.CancelledJobByProvider;
import android.com.provider.apiResponses.checkInRequest.ProviderCheckInRequest;
import android.com.provider.apiResponses.checkInRequest.ProviderCheckOutRequest;
import android.com.provider.apiResponses.completedJobApiResposne.CompletedJobs;
import android.com.provider.apiResponses.currentJobApiResponse.CurrentJob;
import android.com.provider.apiResponses.displayProviderProfile.DisplayProfile;
import android.com.provider.apiResponses.getAddressProfileUpdated.AddressProfileUpdated;
import android.com.provider.apiResponses.getChangePassword.ChangePassword;
import android.com.provider.apiResponses.getCityListApi.CityList;
import android.com.provider.apiResponses.getFAQ.FAQ;
import android.com.provider.apiResponses.getForgetPassword.ForgetPassword;
import android.com.provider.apiResponses.getMatchOTPApi.MatchOtp;
import android.com.provider.apiResponses.getOffredServices.OffrredServices;
import android.com.provider.apiResponses.getOnOffApi.OnOff;
import android.com.provider.apiResponses.getProfileApi.Profile;
import android.com.provider.apiResponses.getResetPasswordApi.ResetPassword;
import android.com.provider.apiResponses.getServiceList.GetServiceList;
import android.com.provider.apiResponses.getServiceTypeNames.ServiceTypeNames;
import android.com.provider.apiResponses.getSiginApi.SignIn;
import android.com.provider.apiResponses.getStateListApi.GetStateList;
import android.com.provider.apiResponses.getUpdateProBio.UpdateBio;
import android.com.provider.apiResponses.getZipCodeListApi.ZipCodeList;
import android.com.provider.apiResponses.providerFullDetails.ProviderFullDetails;
import android.com.provider.apiResponses.providerLogout.ProviderLogoutRequest;
import android.com.provider.apiResponses.refferalCode.RefferalCode;
import android.com.provider.apiResponses.signupApi.SignUp;
import android.com.provider.apiResponses.UpdateBioServiceModel;
import android.com.provider.dto.sr.ServiceRequest;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
public interface RemoteRepositoryService {


    // SIGN IN API    --- 1

    @FormUrlEncoded
    @POST("provider/providerLogin")
    Observable<SignIn> signInAPI(@Field("email") String email,
                                 @Field("password") String password,
                                 @Field("device_id") String device_id);


    // SIGNUP API ---- 2

    @FormUrlEncoded
    @POST("provider/providerRegister")
    Call<SignUp> signUpAPI(@Field("first_name") String first_name,
                           @Field("last_name") String last_name,
                           @Field("email") String email,
                           @Field("password") String password,
                           @Field("mobile") String mobile,
                           @Field("country") String country,
                           @Field("state") String state,
                           @Field("city") String city,
                           @Field("zipCode") String zipCode,
                           @Field("address") String address,
                           @Field("device_id") String device_id,
                           @Field("device_type") String device_type,
                           @Field("role") String role,
                           @Field("service") String service);


    // FORGET PASSWORD API     ------ 3

    @FormUrlEncoded
    @POST("provider/forgotPassword")
    Observable<ForgetPassword> getForgetPasswordAPI(@Field("email") String email);


    // MATCH OTP API  ---------4

    @FormUrlEncoded
    @POST("provider/matchOTP")
    Observable<MatchOtp> getMatchOtpAPI(@Field("userId") String userId,
                                        @Field("otp") String otp);


    // RESET PASSWORD API ---5

    @FormUrlEncoded
    @POST("provider/resetPassword")
    Observable<ResetPassword> getResetPasswordAPI(@Field("email") String email,
                                                  @Field("password") String password);


    // SERVICE-LIST API   ---6

    @GET("provider/getServiceList/en")
    Call<GetServiceList> getServiceListAPI();


    // INSERT UPDATE PROFILE PIC API  -----------7
    // https://medium.com/@adinugroho/upload-image-from-android-app-using-retrofit-2-ae6f922b184c


    @Multipart
    @POST("provider/insertUpdateProfilePic")
    Call<Profile> postImage(@Part("id") RequestBody id, // String will also take
                            @Part MultipartBody.Part image);


    // STATE-LIST API      ----8

    @GET("provider/getStateList")
    Observable<GetStateList> getStateListAPI();

    // CITY-LIST API ----------9

    @FormUrlEncoded
    @POST("provider/getCityList")
    Observable<CityList> getCitiesAPI(@Field("state_id") String state_id);


    // ZIPCODE-LIST API  -----10

    @FormUrlEncoded
    @POST("provider/getZipcodeList")
    Observable<ZipCodeList> getZipCodeListAPI(@Field("city_id") String city_id);


    // UPDATE BIO API  ------------11
    @POST("provider/addUpdateProviderBio")
    Observable<UpdateBio> getUpdateBioAPI(@Body SendBioModel updateBioServiceModel);


    // ----------------12

    @POST("provider/addUpdateProviderBio")
    Observable<UpdateBio> getUpdateBioAPIWithJsonArray(@Body ServiceRequest serviceRequest);


    // CHANGE PASSWORD API     -------13
    @FormUrlEncoded
    @POST("provider/changePassword")
    Observable<ChangePassword> getChangePaaswordAPI(@Field("id") String id,
                                                    @Field("old_password") String old_password,
                                                    @Field("new_password") String new_password);

    @FormUrlEncoded
    @POST("provider/displayApprovedProviderBio")
    Observable<GetProviderBio> getProviderBio(@Field("id")String id);
    // ALL TYPE SERVICE - (OFFERED SERVICES) -------14


    @GET("provider/getAllServiceTypeList/en")
    Observable<OffrredServices> getOffrredServicesAPI();
    //  DISPLAY PROVIDER PROFILE PICTURE  --------15

    @FormUrlEncoded
    @POST("provider/DisplayProvideProfilePicture")
    Call<DisplayProfile> displayProviderImage(@Field("id") String id);


    // CHANGE PASSWORD API   -----------16
    @FormUrlEncoded
    @POST("provider/getServiceTypeName/en")
    Observable<ServiceTypeNames> serviceTypeNamesss(@Field("serviceId") String serviceId);


    // ACTIVE AND INACTIVE API    ----------17

    @FormUrlEncoded
    @POST("provider/activedeactiveseriveprovider")
    Observable<OnOff> onOffApi(@Field("id") String id);


    // MARK: FAQ API   ------18


    @GET("provider/getProviderFaQ/en")
    Observable<FAQ> getFAQAPI();


    // ADDRESS AND PROFILE UPDATED   ------------19


    @FormUrlEncoded
    @POST("provider/updateProviderProfile")
    Call<AddressProfileUpdated> addressProfileUpdated(@Field("id") String id,
                                                      @Field("first_name") String first_name,
                                                      @Field("last_name") String last_name,
                                                      @Field("mobile") String mobile,
                                                      @Field("address") String address,
                                                      @Field("state") String state,
                                                      @Field("city") String city,
                                                      @Field("zipCode") String zipCode,
                                                      @Field("service") String service);


    // for testing
    @FormUrlEncoded
    @POST("provider/updateProviderProfile")
    Observable<AddressProfileUpdated> addressProfileUpdatedRx(@Field("id") String id,
                                                              @Field("first_name") String first_name,
                                                              @Field("last_name") String last_name,
                                                              @Field("mobile") String mobile,
                                                              @Field("address") String address,
                                                              @Field("state") String state,
                                                              @Field("city") String city,
                                                              @Field("zipCode") String zipCode,
                                                              @Field("service") String service);


// ACCEPT INSTANT BOOKING API ----------------20

    @FormUrlEncoded
    @POST("provider/acceptInstantJob")
    Observable<AcceptInstantBooking> acceptInstantBooking(@Field("job_id") String job_id,
                                                          @Field("provider_id") String provider_id);

    // CURRENT JOB--------21

    @FormUrlEncoded
    @POST("provider/ListOfCurrentJobsForProvider/{locale}")
    Observable<CurrentJob> currentJob(@Path("locale") String soapString, @Field("provider_id") String provider_id);


    // CANCELLED JOB ---------22

    @FormUrlEncoded
    @POST("provider/ListOfCanceledJobsFromProvider/{locale}")
    Observable<CancelledJob> cancelledJob(@Path("locale") String language,  @Field("provider_id") String provider_id);


    // COMPLETED JOB-------23

    @FormUrlEncoded
    @POST("provider/ListOfCompletedJobsForProvider/{local}")
    Observable<CompletedJobs> completedJob(@Path("local") String language,@Field("provider_id") String provider_id);


    // CANCELLED JOB BY PROVIDER -----------24


    @FormUrlEncoded
    @POST("provider/CanceljobByProvider")
    Observable<CancelledJobByProvider> cancelledJobByProvider(@Field("job_id") String job_id,
                                                              @Field("provider_id") String provider_id);


    // PROVIDER FULL DETAILS-------------25

    @FormUrlEncoded
    @POST("provider/ProviderfullDetails")
    Observable<ProviderFullDetails> providerFullDetails(@Field("id") String id);


    // REFFERAL CODE
//provider_logout
    @FormUrlEncoded
    @POST("provider/showproviderReferralcode")
    Observable<RefferalCode> referralCode(@Field("provider_id") String provider_id);


    @FormUrlEncoded
    @POST("provider/provider_logout")
    Observable<ProviderLogoutRequest> logoutApi(@Field("id") int provider_id);



    @FormUrlEncoded
    @POST("provider/checkInForJob")
    Observable<ProviderCheckInRequest> checkInApi(@Field("Job_id") String Job_id,
                                                  @Field("provider_id") String provider_id,
                                                  @Field("date") String date,
                                                  @Field("time") String time);



    @FormUrlEncoded
    @POST("provider/checkOutFromJob")
    Observable<ProviderCheckOutRequest> checkOutApi(@Field("Job_id") String Job_id,
                                                    @Field("provider_id") String provider_id,
                                                    @Field("date") String date,
                                                    @Field("time") String time);
}
