package com.clark.android;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Set;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.FragmentManager;
import android.app.KeyguardManager;
import android.app.LoaderManager;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.clark.android.annotation.IntentExtra;
import com.clark.android.annotation.SystemManager;

final class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    static void setFieldFromBundle(Object instance, String name, Class<?> type,
            Field field, Bundle outState) {

        try {
            // int
            if (type == int.class) {
                field.setInt(instance, outState.getInt(name));
            } else if (type == Integer.class) {
                field.set(instance, outState.getInt(name));
            } else if (type == int[].class) {
                field.set(instance, outState.getIntArray(name));
            }

            // long
            else if (type == long.class) {
                field.setLong(instance, outState.getLong(name));
            } else if (type == Long.class) {
                field.set(instance, outState.getLong(name));
            } else if (type == long[].class) {
                field.set(instance, outState.getLongArray(name));
            }

            // boolean
            else if (type == boolean.class) {
                field.setBoolean(instance, outState.getBoolean(name));
            } else if (type == Boolean.class) {
                field.set(instance, outState.getBoolean(name));
            } else if (type == boolean[].class) {
                field.set(instance, outState.getBooleanArray(name));
            }

            // short
            else if (type == short.class) {
                field.setShort(instance, outState.getShort(name));
            } else if (type == Short.class) {
                field.set(instance, outState.getShort(name));
            } else if (type == short[].class) {
                field.set(instance, outState.getShortArray(name));
            }

            // char
            else if (type == char.class) {
                field.setChar(instance, outState.getChar(name));
            } else if (type == Character.class) {
                field.set(instance, outState.getChar(name));
            } else if (type == char[].class) {
                field.set(instance, outState.getCharArray(name));
            }

            // byte
            else if (type == byte.class) {
                field.setByte(instance, outState.getByte(name));
            } else if (type == Byte.class) {
                field.set(instance, outState.getByte(name));
            } else if (type == byte[].class) {
                field.set(instance, outState.getByteArray(name));
            }

            // float
            else if (type == float.class) {
                field.setFloat(instance, outState.getFloat(name));
            } else if (type == Float.class) {
                field.set(instance, outState.getFloat(name));
            } else if (type == float[].class) {
                field.set(instance, outState.getFloatArray(name));
            }

            // double
            else if (type == double.class) {
                field.setDouble(instance, outState.getDouble(name));
            } else if (type == Double.class) {
                field.set(instance, outState.getDouble(name));
            } else if (type == double[].class) {
                field.set(instance, outState.getDoubleArray(name));
            }

            // String
            else if (type == String.class) {
                field.set(instance, outState.getString(name));
            } else if (type == String[].class) {
                field.set(instance, outState.getStringArray(name));
            }

            // Bundle
            else if (type == Bundle.class) {
                field.set(instance, outState.getBundle(name));
            }

            // CharSequence
            else if (CharSequence.class.isAssignableFrom(type)) {
                field.set(instance, outState.getCharSequence(name));
            } else if (CharSequence[].class.isAssignableFrom(type)) {
                field.set(instance, outState.getCharSequenceArray(name));
            }

            // Parcelable
            else if (Parcelable.class.isAssignableFrom(type)) {
                field.set(instance, outState.getParcelable(name));
            } else if (Parcelable[].class.isAssignableFrom(type)) {
                field.set(instance, outState.getParcelableArray(name));
            }

            // Serializable
            else if (Serializable.class.isAssignableFrom(type)) {
                field.set(instance, outState.getSerializable(name));
            }

            // ArrayList
            else if (ArrayList.class.isAssignableFrom(type)) {
                field.set(instance, outState.get(name));
            }

            // Log
            else {
                Log.w(TAG, "restore instances忽略"
                        + instance.getClass().getSimpleName() + "." + name);
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    static void getAndPutValue(Object instance, String name,
            Class<?> fieldType, Field field, Bundle bundle) {
        try {
            // int
            if (fieldType == int.class) {
                int value = field.getInt(instance);
                bundle.putInt(name, value);
            } else if (fieldType == Integer.class) {
                Integer value = (Integer) field.get(instance);
                if (value != null) {
                    bundle.putInt(name, value);
                }
            } else if (fieldType == int[].class) {
                int[] value = (int[]) field.get(instance);
                bundle.putIntArray(name, value);
            }

            // long
            else if (fieldType == long.class) {
                long value = field.getLong(instance);
                bundle.putLong(name, value);
            } else if (fieldType == Long.class) {
                Long value = (Long) field.get(instance);
                if (value != null) {
                    bundle.putLong(name, value);
                }
            } else if (fieldType == long[].class) {
                long[] value = (long[]) field.get(instance);
                bundle.putLongArray(name, value);
            }

            // boolean
            else if (fieldType == boolean.class) {
                boolean value = field.getBoolean(instance);
                bundle.putBoolean(name, value);
            } else if (fieldType == Boolean.class) {
                Boolean value = (Boolean) field.get(instance);
                if (value != null) {
                    bundle.putBoolean(name, value);
                }
            } else if (fieldType == boolean[].class) {
                boolean[] value = (boolean[]) field.get(instance);
                bundle.putBooleanArray(name, value);
            }

            // short
            else if (fieldType == short.class) {
                short value = field.getShort(instance);
                bundle.putShort(name, value);
            } else if (fieldType == Short.class) {
                Short value = (Short) field.get(instance);
                if (value != null) {
                    bundle.putShort(name, value);
                }
            } else if (fieldType == short[].class) {
                short[] value = (short[]) field.get(instance);
                bundle.putShortArray(name, value);
            }

            // char
            else if (fieldType == char.class) {
                char value = field.getChar(instance);
                bundle.putChar(name, value);
            } else if (fieldType == Character.class) {
                Character value = (Character) field.get(instance);
                if (value != null) {
                    bundle.putChar(name, value);
                }
            } else if (fieldType == char[].class) {
                char[] value = (char[]) field.get(instance);
                bundle.putCharArray(name, value);
            }

            // byte
            else if (fieldType == byte.class) {
                byte value = field.getByte(instance);
                bundle.putByte(name, value);
            } else if (fieldType == Byte.class) {
                Byte value = (Byte) field.get(instance);
                if (value != null) {
                    bundle.putByte(name, value);
                }
            } else if (fieldType == byte[].class) {
                byte[] value = (byte[]) field.get(instance);
                bundle.putByteArray(name, value);
            }

            // float
            else if (fieldType == float.class) {
                float value = field.getFloat(instance);
                bundle.putFloat(name, value);
            } else if (fieldType == Float.class) {
                Float value = (Float) field.get(instance);
                if (value != null) {
                    bundle.putFloat(name, value);
                }
            } else if (fieldType == float[].class) {
                float[] value = (float[]) field.get(instance);
                bundle.putFloatArray(name, value);
            }

            // double
            else if (fieldType == double.class) {
                double value = field.getDouble(instance);
                bundle.putDouble(name, value);
            } else if (fieldType == Double.class) {
                Double value = (Double) field.get(instance);
                if (value != null) {
                    bundle.putDouble(name, value);
                }
            } else if (fieldType == double[].class) {
                double[] value = (double[]) field.get(instance);
                bundle.putDoubleArray(name, value);
            }

            // String
            else if (fieldType == String.class) {
                String value = (String) field.get(instance);
                bundle.putString(name, value);
            } else if (fieldType == String[].class) {
                String[] value = (String[]) field.get(instance);
                bundle.putStringArray(name, value);
            }

            // Bundle
            else if (fieldType == Bundle.class) {
                Bundle value = (Bundle) field.get(instance);
                bundle.putBundle(name, value);
            }

            // others
            else {
                Object value = field.get(instance);

                // CharSequence
                if (value instanceof CharSequence) {
                    bundle.putCharSequence(name, (CharSequence) value);
                } else if (value instanceof CharSequence[]) {
                    bundle.putCharSequenceArray(name, (CharSequence[]) value);
                }

                // Parcelable
                else if (value instanceof Parcelable) {
                    bundle.putParcelable(name, (Parcelable) value);
                } else if (value instanceof Parcelable[]) {
                    bundle.putParcelableArray(name, (Parcelable[]) value);
                }

                // Serializable
                else if (value instanceof Serializable) {
                    bundle.putSerializable(name, (Serializable) value);
                }

                // List
                else if (value instanceof ArrayList) {
                    ArrayList list = (ArrayList) value;
                    if (list.size() > 0) {
                        Object e = list.get(0);
                        if (e instanceof String) {
                            bundle.putStringArrayList(name,
                                    (ArrayList<String>) value);
                        } else if (e instanceof CharSequence) {
                            bundle.putCharSequenceArrayList(name,
                                    (ArrayList<CharSequence>) value);
                        } else if (e instanceof Parcelable) {
                            bundle.putParcelableArrayList(name,
                                    (ArrayList<? extends Parcelable>) value);
                        } else if (e instanceof Integer) {
                            bundle.putIntegerArrayList(name,
                                    (ArrayList<Integer>) value);
                        } else {
                            Log.w(TAG, "save instances忽略"
                                    + instance.getClass().getSimpleName() + "."
                                    + name);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    static void findSystemManager(Context context, Field field) {
        final SystemManager systemManager = field
                .getAnnotation(SystemManager.class);

        if (systemManager != null) {
            Class<?> type = field.getType();
            try {
                if (type == PackageManager.class) {
                    if (context instanceof Activity) {
                        Activity activity = (Activity) context;
                        field.set(activity, activity.getPackageManager());
                    }
                } else if (type == WindowManager.class) {
                    field.set(context,
                            context.getSystemService(Context.WINDOW_SERVICE));
                } else if (type == LayoutInflater.class) {
                    field.set(context, context
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE));
                } else if (type == ActivityManager.class) {
                    field.set(context,
                            context.getSystemService(Context.ACTIVITY_SERVICE));
                } else if (type == PowerManager.class) {
                    field.set(context,
                            context.getSystemService(Context.POWER_SERVICE));
                } else if (type == AlarmManager.class) {
                    field.set(context,
                            context.getSystemService(Context.ALARM_SERVICE));
                } else if (type == NotificationManager.class) {
                    field.set(context, context
                            .getSystemService(Context.NOTIFICATION_SERVICE));
                } else if (type == KeyguardManager.class) {
                    field.set(context,
                            context.getSystemService(Context.KEYGUARD_SERVICE));
                } else if (type == LocationManager.class) {
                    field.set(context,
                            context.getSystemService(Context.LOCATION_SERVICE));
                } else if (type == SearchManager.class) {
                    field.set(context,
                            context.getSystemService(Context.SEARCH_SERVICE));
                } else if (type == Vibrator.class) {
                    field.set(context,
                            context.getSystemService(Context.VIBRATOR_SERVICE));
                } else if (type == ConnectivityManager.class) {
                    field.set(context, context
                            .getSystemService(Context.CONNECTIVITY_SERVICE));
                } else if (type == WifiManager.class) {
                    field.set(context,
                            context.getSystemService(Context.WIFI_SERVICE));
                } else if (type == InputMethodManager.class) {
                    field.set(context, context
                            .getSystemService(Context.INPUT_METHOD_SERVICE));
                } else if (type == UiModeManager.class) {
                    field.set(context,
                            context.getSystemService(Context.UI_MODE_SERVICE));
                } else if (Build.VERSION.SDK_INT >= 8
                        && type == DownloadManager.class) {
                    field.set(context,
                            context.getSystemService(Context.DOWNLOAD_SERVICE));
                } else if (type == AssetManager.class) {
                    if (context instanceof Activity) {
                        Activity activity = (Activity) context;
                        field.set(activity, activity.getAssets());
                    }
                } else if (Build.VERSION.SDK_INT >= 11
                        && type == FragmentManager.class) {
                    if (context instanceof Activity) {
                        Activity activity = (Activity) context;
                        field.set(activity, activity.getFragmentManager());
                    }
                } else if (Build.VERSION.SDK_INT >= 11
                        && type == LoaderManager.class) {
                    if (context instanceof Activity) {
                        Activity activity = (Activity) context;
                        field.set(activity, activity.getLoaderManager());
                    }
                }

                if (context instanceof FieldHolder) {
                    FieldHolder baseActivity = (FieldHolder) context;
                    baseActivity.getHoldFields().add(field);
                }
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
    }

    static void findIntentExtra(Object instance, Field field, Intent intent) {
        final IntentExtra extra = field.getAnnotation(IntentExtra.class);
        if (extra != null) {
            String key = extra.value();
            if (key == null || key.trim().length() == 0) {
                key = field.getName();
            }

            Class<?> type = field.getType();
            final Bundle extras = intent.getExtras();
            if (extras != null && extras.containsKey(key)) {
                setFieldFromBundle(instance, key, type, field, extras);

                if (instance instanceof FieldHolder) {
                    FieldHolder baseActivity = (FieldHolder) instance;
                    baseActivity.getHoldFields().add(field);
                }
            }
        }
    }

    static void fieldHolderOnDestroy(FieldHolder fieldHolder) {
        final Set<Field> fields = fieldHolder.getHoldFields();
        if (fields != null && fields.size() > 0) {
            Class<?> type = null;
            for (Field field : fields) {
                type = field.getType();
                if (!type.isPrimitive()) {
                    try {
                        field.set(fieldHolder, null);
                    } catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                }
            }
        }
    }
}
