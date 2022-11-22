package com.evelyne.labs.trialapp.listeners;

import com.evelyne.labs.trialapp.model.Upload;

import java.util.List;

public interface IBookLoadListener {
    void  onUploadLoadSuccess(List<Upload> uploadModelList);
    void onUploadLoadFailure (String Message);
}
