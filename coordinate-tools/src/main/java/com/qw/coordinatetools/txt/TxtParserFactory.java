package com.qw.coordinatetools.txt;

import com.esri.core.geometry.Operator;
import com.qw.coordinatetools.txt.operatorImp.TxtKcdjOperator;

import java.util.HashMap;
import java.util.Map;

/**
 * txt 界址点 解析类
 */
public class TxtParserFactory {
    private static final TxtParserFactory instance = new TxtParserFactory();
    private static final Map<TxtOperator.Type, TxtOperator> st_supportedOperators = new HashMap();

    static {
        st_supportedOperators.put(TxtOperator.Type.kcdj, new TxtKcdjOperator());
    }

    public TxtOperator getOperator(TxtOperator.Type type) {
        if (st_supportedOperators.containsKey(type)) {
            return st_supportedOperators.get(type);
        } else {
            throw new IllegalArgumentException();
        }
    }

    //+++++++++++++++++++单例++++++++++++++++++++++++++++++
    private TxtParserFactory() {
    }

    public static TxtParserFactory getInstance() {
        return instance;
    }

}
