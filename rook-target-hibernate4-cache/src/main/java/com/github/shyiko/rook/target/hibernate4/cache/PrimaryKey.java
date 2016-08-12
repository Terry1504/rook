/*
 * Copyright 2013 Stanley Shyiko
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.shyiko.rook.target.hibernate4.cache;

import org.hibernate.mapping.Collection;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.Component;
import org.hibernate.mapping.KeyValue;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.Table;
import org.hibernate.type.EmbeddedComponentType;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;

/**
 * @author <a href="mailto:stanley.shyiko@gmail.com">Stanley Shyiko</a>
 */
public class PrimaryKey {

    private Class entityClass;
    private final KeyColumn[] positionWithinRow;

    public PrimaryKey(Collection collection, Map<String, Integer> columnIndexByNameMap) {
        this(collection.getKey(), collection.getCollectionTable(), columnIndexByNameMap);
        if (positionWithinRow.length != 1) {
            throw new IllegalStateException("Unexpected PK length " + positionWithinRow.length);
        }
    }

    public PrimaryKey(PersistentClass persistentClass, Map<String, Integer> columnIndexByNameMap) {
        this(persistentClass.getKey(), persistentClass.getTable(), columnIndexByNameMap);
        final Type type = persistentClass.getIdentifier().getType();
        if (type instanceof EmbeddedComponentType) {
            entityClass = type.getReturnedClass();
        } else {
            entityClass = persistentClass.getMappedClass();
        }
    }

    private PrimaryKey(KeyValue keyValue, Table table, Map<String, Integer> columnIndexByNameMap) {
        KeyColumn[] positionWithinRow = new KeyColumn[keyValue.getColumnSpan()];
        int index = 0;
        if (keyValue instanceof Component) {
            Iterator propertyIterator = ((Component) keyValue).getPropertyIterator();
            while (propertyIterator.hasNext()) {
                Property property = (Property) propertyIterator.next();
                String columnName = ((Column) property.getColumnIterator().next()).getName();
                positionWithinRow[index++] = new KeyColumn(property.getName(), columnIndexByNameMap.get(columnName));
            }
        } else {
            Iterator columnIterator = keyValue.getColumnIterator();
            while (columnIterator.hasNext()) {
                String columnName = ((Column) columnIterator.next()).getName();
                positionWithinRow[index++] = new KeyColumn(columnName, columnIndexByNameMap.get(columnName));
            }
        }
        if (positionWithinRow.length == 0) {
            throw new IllegalStateException("Unable to determine PK for " + table.getName());
        }
        this.positionWithinRow = positionWithinRow;
    }

    public Serializable getIdentifier(Serializable[] row) {
        if (positionWithinRow.length == 1) {
            return row[positionWithinRow[0].index];
        }
        try {
            Serializable identifier = (Serializable) entityClass.newInstance();
            for (KeyColumn keyColumn : positionWithinRow) {
                Field field = entityClass.getDeclaredField(keyColumn.name);
                field.setAccessible(true);
                field.set(identifier, row[keyColumn.index]);
            }
            return identifier;
        } catch (Throwable e) {
            throw new IllegalStateException("Unable to instantiate entity key", e);
        }
    }

    /**
     * Class that contains single key column data.
     */
    private static final class KeyColumn {

        private final String name;
        private final int index;

        private KeyColumn(String name, int index) {
            this.name = name;
            this.index = index;
        }
    }
}
