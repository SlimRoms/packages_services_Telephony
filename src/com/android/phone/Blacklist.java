/*
 * Copyright (C) 2015 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.phone;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.Telephony;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.util.HashSet;

/**
 * This class used to handle the blacklist data. Its
 * only remaining purpose is legacy data migration
 */
class Blacklist {
    private static class PhoneNumber implements Externalizable {
        static final long serialVersionUID = 32847013274L;
        String phone;

        public PhoneNumber() {
        }

        public void writeExternal(ObjectOutput out) throws IOException {
        }

        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            phone = (String) in.readObject();
        }

        @Override
        public int hashCode() {
            return phone != null ? phone.hashCode() : 0;
        }
    }

    private static final String BLFILE = "blacklist.dat";
    private static final int BLFILE_VER = 1;

    public static void migrateOldDataIfPresent(Context context) {
        ObjectInputStream ois = null;
        HashSet<PhoneNumber> data = null;

        try {
            ois = new ObjectInputStream(context.openFileInput(BLFILE));
            Object o = ois.readObject();
            if (o != null && o instanceof Integer) {
                // check the version
                Integer version = (Integer) o;
                if (version == BLFILE_VER) {
                    Object numbers = ois.readObject();
                    if (numbers instanceof HashSet) {
                        data = (HashSet<PhoneNumber>) numbers;
                    }
                }
            }
        } catch (IOException e) {
            // Do nothing
        } catch (ClassNotFoundException e) {
            // Do nothing
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    // Do nothing
                }
                context.deleteFile(BLFILE);
            }
        }
        if (data != null) {
            ContentResolver cr = context.getContentResolver();
            ContentValues cv = new ContentValues();
            cv.put(Telephony.Blacklist.PHONE_MODE, 1);

            for (PhoneNumber number : data) {
                Uri uri = Uri.withAppendedPath(
                        Telephony.Blacklist.CONTENT_FILTER_BYNUMBER_URI, number.phone);
                cv.put(Telephony.Blacklist.NUMBER, number.phone);
                cr.update(uri, cv, null, null);
            }
        }
    }
}
