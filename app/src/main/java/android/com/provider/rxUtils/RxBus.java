package android.com.provider.rxUtils;

import android.com.provider.models.OpenApportunity;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class RxBus {


    public PublishSubject<OpenApportunity> openApportunityPublishSubject = PublishSubject.create();

    public void send(OpenApportunity openApportunity){
        openApportunityPublishSubject.onNext(openApportunity);
    }

    public Observable<OpenApportunity> toObservable() {

        return openApportunityPublishSubject;
    }





}
