package com.androidbook.simpleservice;

import com.androidbook.simpleservice.GPXPoint;


interface IRemoteInterface {

    Location getLastLocation();
    GPXPoint getGPXPoint();
}
