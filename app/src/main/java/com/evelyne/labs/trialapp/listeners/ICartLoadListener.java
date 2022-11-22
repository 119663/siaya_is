package com.evelyne.labs.trialapp.listeners;

import com.evelyne.labs.trialapp.model.CartModel;

import java.util.List;

public interface ICartLoadListener {
    void  onCartLoadSuccess(List<CartModel> cartModelList);
    void onCartLoadFailure (String Message);
}
