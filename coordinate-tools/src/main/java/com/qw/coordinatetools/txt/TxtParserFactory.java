package com.qw.coordinatetools.txt;

import com.qw.coordinatetools.txt.operatorImp.CommonOperator;
import com.qw.coordinatetools.txt.operatorImp.TxtKcdjOperator;
import com.qw.coordinatetools.txt.operatorImp.TxtNzyOperator;

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
        st_supportedOperators.put(TxtOperator.Type.stnzy, new TxtNzyOperator());
        st_supportedOperators.put(TxtOperator.Type.commonOperator, new CommonOperator());
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
