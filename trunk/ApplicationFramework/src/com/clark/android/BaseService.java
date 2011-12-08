package com.clark.android;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class BaseService extends Service implements FieldHolder {
    private Class<?> thisClass;
    private final Field[] fields;
    private final Method[] methods;

    private final HashSet<Field> allDelegateFields = new HashSet<Field>();

    public BaseService() {
        thisClass = getClass();
        fields = thisClass.getFields();
        methods = thisClass.getMethods();
    }

    @Override
    public final int onStartCommand(Intent intent, int flags, int startId) {
        processFields(intent);
        return onStartCommand2(intent, flags, startId);
    }

    protected int onStartCommand2(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Utils.fieldHolderOnDestroy(this);
        super.onDestroy();
    }

    @Override
    public final IBinder onBind(Intent intent) {
        processFields(intent);
        return onBind2(intent);
    }

    protected IBinder onBind2(Intent intent) {
        return null;
    }

    private void processFields(Intent intent) {
        if (fields == null || fields.length == 0) {
            return;
        }

        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())
                    || Modifier.isFinal(field.getModifiers())) {
                continue;
            }

            Utils.findSystemManager(this, field);
            Utils.findIntentExtra(this, field, intent);
        }
    }

    @Override
    public final Set<Field> getHoldFields() {
        return allDelegateFields;
    }
}
