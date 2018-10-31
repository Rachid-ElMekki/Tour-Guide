package com.example.lenovo.testmap.Models.Layout;

import android.support.design.widget.FloatingActionButton;
import android.widget.ImageView;

public class Menu
{

    private ImageView hotelButton;
    private ImageView restoButton;
    private ImageView cafeButton;
    private ImageView spaButton;
    private ImageView mallButton;
    private ImageView pharmacyButton;
    private ImageView marketButton;
    private ImageView bankButton;
    private ImageView busButton;
    private ImageView movieButton;

    public Menu(ImageView hotelButton, ImageView restoButton, ImageView cafeButton,
                ImageView spaButton, ImageView mallButton, ImageView pharmacyButton,
                ImageView marketButton, ImageView bankButton, ImageView busButton, ImageView movieButton)
    {
        this.hotelButton = hotelButton;
        this.restoButton = restoButton;
        this.cafeButton = cafeButton;
        this.spaButton = spaButton;
        this.mallButton = mallButton;
        this.pharmacyButton = pharmacyButton;
        this.marketButton = marketButton;
        this.bankButton = bankButton;
        this.busButton = busButton;
        this.movieButton = movieButton;
    }

    public ImageView getHotelButton() {
        return hotelButton;
    }

    public void setHotelButton(ImageView hotelButton) {
        this.hotelButton = hotelButton;
    }

    public ImageView getRestoButton() {
        return restoButton;
    }

    public void setRestoButton(ImageView restoButton) {
        this.restoButton = restoButton;
    }

    public ImageView getCafeButton() {
        return cafeButton;
    }

    public void setCafeButton(ImageView cafeButton) {
        this.cafeButton = cafeButton;
    }

    public ImageView getSpaButton() {
        return spaButton;
    }

    public void setSpaButton(ImageView spaButton) {
        this.spaButton = spaButton;
    }

    public ImageView getMallButton() {
        return mallButton;
    }

    public void setMallButton(ImageView mallButton) {
        this.mallButton = mallButton;
    }

    public ImageView getPharmacyButton() {
        return pharmacyButton;
    }

    public void setPharmacyButton(ImageView pharmacyButton) {
        this.pharmacyButton = pharmacyButton;
    }

    public ImageView getMarketButton() {
        return marketButton;
    }

    public void setMarketButton(ImageView marketButton) {
        this.marketButton = marketButton;
    }

    public ImageView getBankButton() {
        return bankButton;
    }

    public void setBankButton(ImageView bankButton) {
        this.bankButton = bankButton;
    }

    public ImageView getBusButton() {
        return busButton;
    }

    public void setBusButton(ImageView busButton) {
        this.busButton = busButton;
    }

    public ImageView getMovieButton() {
        return movieButton;
    }

    public void setMovieButton(ImageView movieButton) {
        this.movieButton = movieButton;
    }
}
