package com.cditer.free.stateflow.core.impl;

import com.cditer.free.stateflow.core.IBusData;
import com.cditer.free.stateflow.core.IState;
import com.cditer.free.stateflow.core.IStateContext;

/**
 * @author C.H
 * @email chfree365@qq.com
 * @createtime 2022/9/22 12:40
 * @comment
 */

public abstract class DefaultStateContext implements IStateContext {
    private IState state;

    private IBusData busData;

    @Override
    public IState getStatue() {
        return state;
    }

    @Override
    public IBusData getBusData() {
        return busData;
    }

    @Override
    public void setStatue(IState statue) {
        this.state = statue;
    }

    @Override
    public void setBusData(IBusData busData) {
        this.busData = busData;
    }

    @Override
    public void doWork() {
        this.state.doWork(this);
    }
}
