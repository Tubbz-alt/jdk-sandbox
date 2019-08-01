/*
 * Copyright (c) 2019, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package jdk.jfr.consumer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jdk.jfr.EventType;
import jdk.jfr.ValueDescriptor;

final class ObjectContext {
    private final Map<ValueDescriptor, ObjectContext> contextLookup;

    final EventType eventType;
    final List<ValueDescriptor> fields;
    final TimeConverter timeConverter;

    public ObjectContext(EventType eventType, List<ValueDescriptor> fields, TimeConverter timeConverter) {
        this.contextLookup = new HashMap<>();
        this.eventType = eventType;
        this.fields = fields;
        this.timeConverter = timeConverter;
    }

    private ObjectContext(ObjectContext parent, ValueDescriptor desc) {
        this.eventType = parent.eventType;
        this.contextLookup = parent.contextLookup;
        this.timeConverter = parent.timeConverter;
        this.fields = desc.getFields();
    }

    public ObjectContext getInstance(ValueDescriptor desc) {
        ObjectContext h = contextLookup.get(desc);
        if (h == null) {
            h = new ObjectContext(this, desc);
            contextLookup.put(desc, h);
        }
        return h;
    }
}