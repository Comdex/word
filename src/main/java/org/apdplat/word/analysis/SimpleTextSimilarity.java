/**
 *
 * APDPlat - Application Product Development Platform
 * Copyright (c) 2013, 杨尚川, yang-shangchuan@qq.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.apdplat.word.analysis;

import org.apdplat.word.segmentation.Word;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 文本相似度计算
 * 判定方式：简单共有词，通过计算两篇文档有多少个相同的词来评估他们的相似度
 * @author 杨尚川
 */
public class SimpleTextSimilarity extends TextSimilarity {
    /**
     * 判定相似度的方式：简单共有词
     * @param frequency1 文本1的词频统计结果
     * @param frequency2 文本2的词频统计结果
     * @return 相似度分值
     */
    @Override
    protected double scoreImpl(Map<Word, AtomicInteger> frequency1, Map<Word, AtomicInteger> frequency2) {
        //判断有几个相同的词
        AtomicInteger intersectionLength = new AtomicInteger();
        frequency1.keySet().parallelStream().forEach(word -> {
            if (frequency2.keySet().contains(word)) {
                intersectionLength.incrementAndGet();
            }
        });
        double score = intersectionLength.get()/(double)Math.min(frequency1.size(), frequency2.size());
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("文本1有的词数：" + frequency1.size());
            LOGGER.debug("文本2有的词数：" + frequency2.size());
            LOGGER.debug("文本1和2共有的词数：" + intersectionLength.get());
            LOGGER.debug("相似度分值=" + intersectionLength.get() + "/(double)Math.min(" + frequency1.size() + ", " + frequency2.size() + ")=" + score);
        }
        return score;
    }

    public static void main(String[] args) {
        String text1 = "我爱购物";
        String text2 = "我爱读书";
        String text3 = "他是黑客";
        TextSimilarity textSimilarity = new SimpleTextSimilarity();
        double score1pk1 = textSimilarity.similarScore(text1, text1);
        double score1pk2 = textSimilarity.similarScore(text1, text2);
        double score1pk3 = textSimilarity.similarScore(text1, text3);
        double score2pk2 = textSimilarity.similarScore(text2, text2);
        double score2pk3 = textSimilarity.similarScore(text2, text3);
        double score3pk3 = textSimilarity.similarScore(text3, text3);
        System.out.println(text1+" 和 "+text1+" 的相似度分值："+score1pk1);
        System.out.println(text1+" 和 "+text2+" 的相似度分值："+score1pk2);
        System.out.println(text1+" 和 "+text3+" 的相似度分值："+score1pk3);
        System.out.println(text2+" 和 "+text2+" 的相似度分值："+score2pk2);
        System.out.println(text2+" 和 "+text3+" 的相似度分值："+score2pk3);
        System.out.println(text3+" 和 "+text3+" 的相似度分值："+score3pk3);
    }
}
