/*
 * Copyright 2010 the original author or authors.
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

package org.gradle.internal.fingerprint.impl;

import com.google.common.collect.ImmutableSet;
import org.gradle.api.NonNullApi;
import org.gradle.api.file.FileCollection;
import org.gradle.api.internal.file.FileCollectionInternal;
import org.gradle.internal.fingerprint.CurrentFileCollectionFingerprint;
import org.gradle.internal.fingerprint.FileCollectionFingerprint;
import org.gradle.internal.fingerprint.FileCollectionFingerprinter;
import org.gradle.internal.fingerprint.FingerprintingStrategy;
import org.gradle.internal.snapshot.FileSystemLocationSnapshot;
import org.gradle.internal.snapshot.FileSystemSnapshot;
import org.gradle.internal.snapshot.FileSystemSnapshotter;

/**
 * Responsible for calculating a {@link FileCollectionFingerprint} for a particular {@link FileCollection}.
 */
@NonNullApi
public abstract class AbstractFileCollectionFingerprinter implements FileCollectionFingerprinter {
    private final FileSystemSnapshotter fileSystemSnapshotter;
    private final FingerprintingStrategy fingerprintingStrategy;

    public AbstractFileCollectionFingerprinter(FingerprintingStrategy fingerprintingStrategy, FileSystemSnapshotter fileSystemSnapshotter) {
        this.fileSystemSnapshotter = fileSystemSnapshotter;
        this.fingerprintingStrategy = fingerprintingStrategy;
    }

    @Override
    public CurrentFileCollectionFingerprint fingerprint(FileCollection files) {
        FileCollectionInternal fileCollection = (FileCollectionInternal) files;
        ImmutableSet<FileSystemSnapshot> roots = fileSystemSnapshotter.snapshot(fileCollection);
        return DefaultCurrentFileCollectionFingerprint.from(roots, fingerprintingStrategy);
    }

    @Override
    public CurrentFileCollectionFingerprint fingerprint(Iterable<? extends FileSystemSnapshot> roots) {
        return DefaultCurrentFileCollectionFingerprint.from(roots, fingerprintingStrategy);
    }

    @Override
    public String normalizePath(FileSystemLocationSnapshot root) {
        return fingerprintingStrategy.normalizePath(root);
    }

    @Override
    public CurrentFileCollectionFingerprint empty() {
        return fingerprintingStrategy.getEmptyFingerprint();
    }
}
