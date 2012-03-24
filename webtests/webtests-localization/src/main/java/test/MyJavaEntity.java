/*
 * Copyright 2001-2010 Remi Vankeisbelck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package test;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableId;
import org.compass.annotations.SearchableProperty;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Alexis Boissonnat - alexis.boissonnat 'at' gmail.com
 */
@Entity
@Searchable
public class MyJavaEntity {

    @Id
    @SearchableId
    private Long id;

    @NotNull
    @SearchableProperty
    private String name;

    @Min(2)
    @SearchableProperty
    private int minNumber;

    @Max(50)
    @SearchableProperty
    private int maxNumber;

    @Size(min=2, max=10)
    private String size;

    @Range(min=2, max=10)
    private int range;

    @Email
    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMinNumber() {
        return minNumber;
    }


    public void setMinNumber(int minNumber) {
        this.minNumber = minNumber;
    }

    public int getMaxNumber() {
        return maxNumber;
    }

    public void setMaxNumber(int maxNumber) {
        this.maxNumber = maxNumber;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
