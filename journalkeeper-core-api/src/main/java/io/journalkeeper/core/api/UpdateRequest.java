/**
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
package io.journalkeeper.core.api;

/**
 * @author LiYue
 * Date: 2019/11/13
 */
public class UpdateRequest<E> {
    // Entry
    private final E entry;
    // 分区
    private final int partition;
    // 批量大小
    private final int batchSize;

    public UpdateRequest(E entry, int partition, int batchSize) {
        this.entry = entry;
        this.partition = partition;
        this.batchSize = batchSize;
    }

    public UpdateRequest(E entry) {
        this(entry, RaftJournal.DEFAULT_PARTITION, 1);
    }

    public E getEntry() {
        return entry;
    }

    public int getPartition() {
        return partition;
    }

    public int getBatchSize() {
        return batchSize;
    }

}
