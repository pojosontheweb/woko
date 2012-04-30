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

import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class FolderCopyTest {

    @Test
    public void testCopy() throws Exception {
        File tmpDir = new File(System.getProperty("java.io.tmpdir") + File.separator + "copyTest" +
                File.separator + Long.toString(System.currentTimeMillis()));
        File from = new File(tmpDir.getAbsolutePath() + File.separator + "/from");
        File to = new File(tmpDir.getAbsolutePath() + File.separator + "/to");
        to.mkdirs();

        // populate "from" dir
        List<String> files = new ArrayList<String>();
        files.add("/a.txt");
        files.add("/b.txt");
        files.add("/test/c.txt");
        files.add("/test/d.txt");
        files.add("/test/sub/e.txt");
        files.add("/test/sub/f.txt");
        for (String file : files) {
            File f = new File(from.getAbsolutePath() + File.separator + file);
            new File(f.getParent()).mkdirs();
            FileOutputStream fos = new FileOutputStream(f);
            try {
                fos.write("this is a test".getBytes());
            } finally {
                fos.flush();
                fos.close();
            }

        }

        final List<File> copied = new ArrayList<File>();
        FolderCopy.copy(from, to, new FolderCopy.CopyCallback() {
            @Override
            public void onCopy(File fromDir, File toDir, File f) {
                copied.add(f);
            }
        });
        assertEquals("unexpected file count", 6, copied.size());

    }


}
