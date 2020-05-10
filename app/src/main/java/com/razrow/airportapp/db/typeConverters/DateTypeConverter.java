package com.razrow.airportapp.db.typeConverters;

import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.util.Date;

public class DateTypeConverter {
    @TypeConverter
    public long convertDateToLong(Date date){
        return date.getTime();
    }

    @TypeConverter
    public Date convertLongToDate(long time){
        return new Date(time);
    }
}
