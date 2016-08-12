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
package com.github.shyiko.rook.it.h4com.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="mailto:stanley.shyiko@gmail.com">Stanley Shyiko</a>
 */
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@javax.persistence.Entity
public class RootEntity {

    @Id
    @GeneratedValue
    private long id;
    @Column
    private String name;
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @OneToOne(cascade = {
        CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH
    })
    private OneToOneEntity oneToOneEntity;
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @LazyCollection(LazyCollectionOption.TRUE)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @OneToMany(cascade = {
        CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH
    }, fetch = FetchType.LAZY)
    private Set<OneToManyEntity> oneToManyEntities;
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @LazyCollection(LazyCollectionOption.TRUE)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @OneToMany(cascade = {
        CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH
    }, fetch = FetchType.LAZY, mappedBy = "rootEntity")
    private Set<JoinedOneToManyEntity> joinedOneToManyEntities;
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @OneToMany(mappedBy = "rootId", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private Set<CompositeKeyEntity> compositeRelations = new HashSet<CompositeKeyEntity>();

    public RootEntity() {
    }

    public RootEntity(String name) {
        this.name = name;
    }

    public RootEntity(String name, OneToOneEntity oneToOneEntity, Set<OneToManyEntity> oneToManyEntities) {
        this.name = name;
        this.oneToOneEntity = oneToOneEntity;
        this.oneToManyEntities = oneToManyEntities;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OneToOneEntity getOneToOneEntity() {
        return oneToOneEntity;
    }

    public void setOneToOneEntity(OneToOneEntity oneToOneEntity) {
        this.oneToOneEntity = oneToOneEntity;
    }

    public Set<OneToManyEntity> getOneToManyEntities() {
        return oneToManyEntities;
    }

    public void setOneToManyEntities(Set<OneToManyEntity> oneToManyEntities) {
        this.oneToManyEntities = oneToManyEntities;
    }

    public Set<JoinedOneToManyEntity> getJoinedOneToManyEntities() {
        return joinedOneToManyEntities;
    }

    public void setJoinedOneToManyEntities(Set<JoinedOneToManyEntity> joinedOneToManyEntities) {
        for (JoinedOneToManyEntity directedOneToManyEntity : joinedOneToManyEntities) {
            directedOneToManyEntity.setRootEntity(this);
        }
        this.joinedOneToManyEntities = joinedOneToManyEntities;
    }

    public Set<CompositeKeyEntity> getCompositeRelations() {
        return compositeRelations;
    }

    public void setCompositeRelations(Set<CompositeKeyEntity> compositeRelations) {
        this.compositeRelations = compositeRelations;
    }
}
