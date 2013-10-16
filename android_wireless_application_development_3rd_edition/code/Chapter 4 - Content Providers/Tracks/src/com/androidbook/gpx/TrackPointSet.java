package com.androidbook.gpx;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import android.util.Log;

import com.google.android.maps.GeoPoint;

public class TrackPointSet {
    public static class TrackPoint {
        private GeoPoint point;
        private Date timestamp;
        private double elevation;

        /**
         * @param point
         * @param timestamp
         * @param elevation
         */
        public TrackPoint(GeoPoint point, Date timestamp, double elevation) {
            this.point = point;
            this.timestamp = timestamp;
            this.elevation = elevation;
        }

        /**
         * @param lat
         * @param lon
         * @param date
         * @param elevation
         * @throws ParseException
         */
        public TrackPoint(String lat, String lon, String date, String elevation) throws ParseException {
            // the location
            setPoint(lat, lon);
            // the date
            setTimestamp(date);
            // the elevation
            setElevation(elevation);
        }

        public TrackPoint() {
            // nothing to do
        }

        /**
         * @return the point
         */
        public GeoPoint getPoint() {
            return point;
        }

        /**
         * @param point
         *            the point to set
         */
        public void setPoint(GeoPoint point) {
            this.point = point;
        }

        /**
         * @param lat
         * @param lon
         */
        public void setPoint(String lat, String lon) {
            this.point = new GeoPoint((int) (Double.parseDouble(lat) * 1000000),
                    (int) Double.parseDouble(lon) * 1000000);
        }

        /**
         * @return the timestamp
         */
        public Date getTimestamp() {
            return timestamp;
        }

        /**
         * @param timestamp
         *            the timestamp to set
         */
        public void setTimestamp(Date timestamp) {
            this.timestamp = timestamp;
        }

        public void setTimestamp(String date) throws ParseException {
            // 2008-06-26T16:10:32Z
            SimpleDateFormat sdf = new SimpleDateFormat("y'-'M'-'d'T'H':'m':'s'Z'");
            this.timestamp = sdf.parse(date);
            Log.d("GPX", "date = " + this.timestamp.toString());
        }

        /**
         * @return the elevation
         */
        public double getElevation() {
            return elevation;
        }

        /**
         * @param elevation
         *            the elevation to set
         */
        public void setElevation(double elevation) {
            this.elevation = elevation;
        }

        /**
         * @param elevation2
         */
        private void setElevation(String elevation) {
            this.elevation = Double.parseDouble(elevation);
        }
    }

    private Vector<TrackPoint> trackPoints = new Vector<TrackPoint>();

    public void addPoint(TrackPoint point) {
        trackPoints.add(point);
    }

    public Vector<TrackPoint> getPoints() {
        return trackPoints;
    }
}
