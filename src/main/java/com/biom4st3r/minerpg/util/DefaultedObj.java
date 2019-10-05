package com.biom4st3r.minerpg.util;

import com.biom4st3r.biow0rks.Biow0rks;

public class DefaultedObj<T> {
    private T defaultValue;
    private T value;

    public DefaultedObj(T value, T defaultValue) {
        if(defaultValue == null)
        {
            Biow0rks.error("A NNObj has been assigned null;\n%s\nNNObj's should never be assigned null", new Exception().getCause().getMessage());
        }

        this.defaultValue = defaultValue;
        if(value == null)
            value = defaultValue;
        else
            this.value = value;
    }

    public void reset() {
        this.value = defaultValue;
    }

    public void set(T value) {
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    private T getDefault() throws InstantiationException, IllegalAccessException {
        if (defaultValue == null) {
            defaultValue = (T) defaultValue.getClass().newInstance();
        }
        return defaultValue;
    }

    public T getValue() {
        if (this.value == null) {
            try {
                this.value = this.getDefault();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        try
        {
            if(((T)obj) == this.value)
            {
                return true;
            }

        }
        catch(Exception e)
        {
            return false;
        }
        return false;
    }
}