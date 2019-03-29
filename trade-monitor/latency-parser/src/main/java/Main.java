/*
 * Copyright (c) 2008-2017, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            InputStream stream = Main.class.getResourceAsStream("usage.txt");
            copy(stream);
            System.exit(1);
        }
        File[] files = Paths.get(args[1]).toFile().listFiles();
        if (files == null) {
            System.err.println("No sub-directory found at " + args[1]);
            System.exit(1);
        }
        Path path = null;
        for (File file : files) {
            if (file.getName().startsWith(args[0])) {
                path = file.toPath();
                break;
            }
        }

        if (path == null) {
            System.err.println("No sub-directory found for " + args[0] + " in " + args[1]);
            System.exit(1);
        }

        switch (args[0]) {
            case "jet":
                AvgLatencyParserJet.parseJet(path);
                return;
            case "flink":
                AvgLatencyParserFlink.parseFlink(path);
                return;
            case "spark":
                AvgLatencyParserSpark.parseSpark(path);
                return;
            default:
                System.err.println("Unknown first argument: " + args[0]);
                System.exit(1);
        }
    }

    private static void copy(InputStream stream) throws IOException {
        try (BufferedReader r = new BufferedReader(new InputStreamReader(stream))) {
            for (String s; (s = r.readLine()) != null; ) {
                System.err.println(s);
            }
        }
    }
}
