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

import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

/**
 * 状态机接口。
 *
 * 状态机负责实现{@link #execute(Object, int, long, int, RaftJournal)} 接口执行操作日志{@link E}，并返回执行结果{@link ER}。
 * 状态机还可以实现{@link State#query(Object, RaftJournal)}查询接口，用于查询状态机中的状态数据。
 *
 * 可选实现：
 * {@link java.io.Flushable}：将状态机中未持久化的输入写入磁盘；
 *
 * @param <Q> 状态查询条件类型
 * @param <QR> 状态查询结果类型
 * @param <E> 操作命令的类型
 * @param <ER> 状态机执行结果类型
 *
 * @author LiYue
 * Date: 2019-03-20
 */
public interface State<E, ER, Q, QR> {
    /**
     * 在状态state上执行命令entries，JournalKeeper保证执行操作命令的线性语义。要求：
     * <ul>
     *     <li>原子性</li>
     *     <li>幂等性</li>
     * </ul>
     * 成功返回执行结果，否则抛异常。
     *
     * @param entry 待执行的命令
     * @param partition 分区
     * @param index entry在Journal中的索引序号
     * @param batchSize 如果当前entry是一个批量entry，batchSize为这批entry的数量，否则为1；
     * @param journal 当前的journal
     * @return 执行结果。See {@link StateResult}
     */
    StateResult<ER> execute(E entry, int partition, long index, int batchSize, RaftJournal journal);

    /**
     * 查询
     * @param query 查询条件
     * @param journal 当前的journal
     * @return 查询结果
     */
    QR query(Q query, RaftJournal journal);

    /**
     * 从磁盘中恢复状态机中的状态数据，在状态机启动的时候调用。
     * @param path 存放state文件的路径
     * @param properties 属性
     * @throws IOException 发生IO异常时抛出
     */
    void recover(Path path, Properties properties) throws IOException;

    /**
     * 安全的关闭状态机
     */
    default void close(){}
}
