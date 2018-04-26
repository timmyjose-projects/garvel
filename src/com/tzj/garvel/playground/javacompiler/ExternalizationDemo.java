package com.tzj.garvel.playground.javacompiler;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

class Foo implements Externalizable {
    private static final long serialVersionUID = -5452584754319759953L;
    private int id;
    private Map<String, String> props;

    public Foo() {
    }

    public Foo(final int id, final Map<String, String> props) {
        this.id = id;
        this.props = props;
    }

    @Override
    public String toString() {
        return "Foo{" +
                "id=" + id +
                ", props=" + props +
                '}';
    }

    public int getId() {
        return id;
    }

    public Map<String, String> getProps() {
        return props;
    }

    @Override
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeInt(id);
        out.writeObject(props);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        id = in.readInt();
        props = (HashMap<String, String>) in.readObject();
    }
}

public class ExternalizationDemo {
    public static void main(String[] args) throws IOException {
        final Map<String, String> props = new HashMap<>();
        props.put("name", "foo");
        props.put("time", "now");
        props.put("path", "here");

        Foo foo = new Foo(1, props);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("/tmp/foo.ser"))) {
            oos.writeObject(foo);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (ObjectInput ois = new ObjectInputStream(new FileInputStream("/tmp/foo.ser"))) {
            Foo anotherFoo = (Foo) ois.readObject();
            System.out.println(anotherFoo);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}


