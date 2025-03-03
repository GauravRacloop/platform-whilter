package com.whilter.pubsub.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.whilter.pubsub.Serializer;
import com.whilter.pubsub.SerializerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;

/**
 * Created by mayank on 22/07/18 11:48 PM.
 */
public class KryoSerializer<T extends Serializable> implements Serializer<T>, SerializerFactory {

    private ThreadLocal<Kryo> kryoThreadLocal;

    private Class<? extends Serializable> serializableClass;

    public KryoSerializer() {
        kryoThreadLocal = ThreadLocal.withInitial(() -> {
            Kryo kryo = new Kryo();

            kryo.register(ValueWrapper.class);
            if (this.serializableClass != null) {
                kryo.register(this.serializableClass);
            } else {
                kryo.setRegistrationRequired(false);
            }
            return kryo;
        });
    }

    public KryoSerializer(Class<? extends Serializable> serializableClass) {
        this();
        this.serializableClass = serializableClass;
    }

    @Override
    public byte[] serialize(T object) {
        Kryo kryo = kryoThreadLocal.get();

        ValueWrapper<T> valueWrapper = new ValueWrapper<>(object);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Output output = new Output(baos);
        kryo.writeObject(output, valueWrapper);
        output.flush();
        return baos.toByteArray();
    }

    @Override
    public T deserialize(byte[] channel) {
        Kryo kryo = kryoThreadLocal.get();
        ValueWrapper<T> valueWrapper;

        InputStream inputStream = new ByteArrayInputStream(channel);
        Input input = new Input(inputStream);

        valueWrapper = kryo.readObject(input, ValueWrapper.class);
        input.close();
        return valueWrapper.value;
    }

    public Object deserialize(Object model) {
        return model;
    }

    @Override
    public Serializer<?> create() {
        return new KryoSerializer();
    }

    @Override
    public Serializer<?> create(Class<? extends Serializable> serializableClass) {
        return new KryoSerializer(serializableClass);
    }

    public static class ValueWrapper<T extends Serializable> implements Serializable {
        private static final long serialVersionUID = 2L;

        private T value;

        public ValueWrapper(T value) {
            this.value = value;
        }

        public ValueWrapper() {
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }
    }

    public static void main(String[] args) {
        KryoSerializer<Serializable> asd = new KryoSerializer<>();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> System.out.println(asd.deserialize(asd.serialize("minku")))).start();
        }
    }
}
