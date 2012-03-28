/*
 * Copyright 2001-2012 Remi Vankeisbelck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package woko.mojos;

import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;

public class FolderCopy {

    public static interface CopyCallback {
        void onCopy(File fromDir, File toDir, File f);
    }

    public static void copy(File fromDir, File toDir, CopyCallback callback) throws IOException {
        copyRecurse(fromDir, toDir, fromDir, callback);
    }

    private static void copyRecurse(File fromDir, File toDir, File f, CopyCallback callback) throws IOException {
        if (f.isDirectory()) {
            for (File child : f.listFiles()) {
                copyRecurse(fromDir, toDir, child, callback);
            }
        } else {
            // do copy
            String toDirPath = toDir.getAbsolutePath();
            String fromDirPath = fromDir.getAbsolutePath();
            String fPath = f.getAbsolutePath();
            String relativePath = fPath.substring(fromDirPath.length(), fPath.length());
            String newPath = toDirPath + relativePath;
            File newFile = new File(newPath);
            if (!newFile.exists()) {
                new File(newFile.getParent()).mkdirs();
            }
            Files.copy(f, newFile);
            if (callback!=null) {
                callback.onCopy(fromDir, toDir, f);
            }
        }
    }

}
