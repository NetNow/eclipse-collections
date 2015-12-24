/*
 * Copyright (c) 2015 Goldman Sachs.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.impl.bag.immutable;

import java.util.Iterator;

import org.eclipse.collections.api.RichIterable;
import org.eclipse.collections.api.bag.ImmutableBag;
import org.eclipse.collections.api.bag.MutableBag;
import org.eclipse.collections.api.bag.primitive.ImmutableBooleanBag;
import org.eclipse.collections.api.bag.primitive.ImmutableByteBag;
import org.eclipse.collections.api.bag.primitive.ImmutableCharBag;
import org.eclipse.collections.api.bag.primitive.ImmutableDoubleBag;
import org.eclipse.collections.api.bag.primitive.ImmutableFloatBag;
import org.eclipse.collections.api.bag.primitive.ImmutableIntBag;
import org.eclipse.collections.api.bag.primitive.ImmutableLongBag;
import org.eclipse.collections.api.bag.primitive.ImmutableShortBag;
import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.api.block.function.Function2;
import org.eclipse.collections.api.block.function.primitive.BooleanFunction;
import org.eclipse.collections.api.block.function.primitive.ByteFunction;
import org.eclipse.collections.api.block.function.primitive.CharFunction;
import org.eclipse.collections.api.block.function.primitive.DoubleFunction;
import org.eclipse.collections.api.block.function.primitive.FloatFunction;
import org.eclipse.collections.api.block.function.primitive.IntFunction;
import org.eclipse.collections.api.block.function.primitive.LongFunction;
import org.eclipse.collections.api.block.function.primitive.ShortFunction;
import org.eclipse.collections.api.block.predicate.Predicate;
import org.eclipse.collections.api.block.predicate.Predicate2;
import org.eclipse.collections.api.block.procedure.Procedure;
import org.eclipse.collections.api.block.procedure.primitive.ObjectIntProcedure;
import org.eclipse.collections.api.collection.MutableCollection;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.api.partition.bag.PartitionImmutableBag;
import org.eclipse.collections.api.partition.bag.PartitionMutableBag;
import org.eclipse.collections.api.tuple.primitive.ObjectIntPair;
import org.eclipse.collections.impl.bag.mutable.primitive.BooleanHashBag;
import org.eclipse.collections.impl.bag.mutable.primitive.ByteHashBag;
import org.eclipse.collections.impl.bag.mutable.primitive.CharHashBag;
import org.eclipse.collections.impl.bag.mutable.primitive.DoubleHashBag;
import org.eclipse.collections.impl.bag.mutable.primitive.FloatHashBag;
import org.eclipse.collections.impl.bag.mutable.primitive.IntHashBag;
import org.eclipse.collections.impl.bag.mutable.primitive.LongHashBag;
import org.eclipse.collections.impl.bag.mutable.primitive.ShortHashBag;
import org.eclipse.collections.impl.block.factory.Functions;
import org.eclipse.collections.impl.block.factory.Predicates;
import org.eclipse.collections.impl.factory.Bags;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.map.mutable.UnifiedMap;
import org.eclipse.collections.impl.partition.bag.PartitionHashBag;

/**
 * @since 1.0
 */
public abstract class AbstractImmutableBag<T>
        extends AbstractImmutableBagIterable<T>
        implements ImmutableBag<T>
{
    public ImmutableBag<T> newWithoutAll(Iterable<? extends T> elements)
    {
        return this.reject(Predicates.in(elements));
    }

    public ImmutableBag<T> toImmutable()
    {
        return this;
    }

    public ImmutableBag<T> tap(Procedure<? super T> procedure)
    {
        this.forEach(procedure);
        return this;
    }

    public <P> ImmutableBag<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.select(Predicates.bind(predicate, parameter));
    }

    public <P> ImmutableBag<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.reject(Predicates.bind(predicate, parameter));
    }

    public PartitionImmutableBag<T> partition(final Predicate<? super T> predicate)
    {
        final PartitionMutableBag<T> partitionMutableBag = new PartitionHashBag<T>();
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                MutableBag<T> bucket = predicate.accept(each)
                        ? partitionMutableBag.getSelected()
                        : partitionMutableBag.getRejected();
                bucket.addOccurrences(each, occurrences);
            }
        });
        return partitionMutableBag.toImmutable();
    }

    public <P> PartitionImmutableBag<T> partitionWith(final Predicate2<? super T, ? super P> predicate, final P parameter)
    {
        final PartitionMutableBag<T> partitionMutableBag = new PartitionHashBag<T>();
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                MutableBag<T> bucket = predicate.accept(each, parameter)
                        ? partitionMutableBag.getSelected()
                        : partitionMutableBag.getRejected();
                bucket.addOccurrences(each, occurrences);
            }
        });
        return partitionMutableBag.toImmutable();
    }

    public <P, V> ImmutableBag<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter)
    {
        return this.collect(Functions.bind(function, parameter));
    }

    public ImmutableBooleanBag collectBoolean(BooleanFunction<? super T> booleanFunction)
    {
        return this.collectBoolean(booleanFunction, new BooleanHashBag()).toImmutable();
    }

    public ImmutableByteBag collectByte(ByteFunction<? super T> byteFunction)
    {
        return this.collectByte(byteFunction, new ByteHashBag()).toImmutable();
    }

    public ImmutableCharBag collectChar(CharFunction<? super T> charFunction)
    {
        return this.collectChar(charFunction, new CharHashBag()).toImmutable();
    }

    public ImmutableDoubleBag collectDouble(DoubleFunction<? super T> doubleFunction)
    {
        return this.collectDouble(doubleFunction, new DoubleHashBag()).toImmutable();
    }

    public ImmutableFloatBag collectFloat(FloatFunction<? super T> floatFunction)
    {
        return this.collectFloat(floatFunction, new FloatHashBag()).toImmutable();
    }

    public ImmutableIntBag collectInt(IntFunction<? super T> intFunction)
    {
        return this.collectInt(intFunction, new IntHashBag()).toImmutable();
    }

    public ImmutableLongBag collectLong(LongFunction<? super T> longFunction)
    {
        return this.collectLong(longFunction, new LongHashBag()).toImmutable();
    }

    public ImmutableShortBag collectShort(ShortFunction<? super T> shortFunction)
    {
        return this.collectShort(shortFunction, new ShortHashBag()).toImmutable();
    }

    public ImmutableList<ObjectIntPair<T>> topOccurrences(int n)
    {
        return this.occurrencesSortingBy(n,
                new IntFunction<ObjectIntPair<T>>()
                {
                    public int intValueOf(ObjectIntPair<T> item)
                    {
                        return -item.getTwo();
                    }
                },
                Lists.fixedSize.<ObjectIntPair<T>>empty()
        ).toImmutable();
    }

    public ImmutableList<ObjectIntPair<T>> bottomOccurrences(int n)
    {
        return this.occurrencesSortingBy(n,
                new IntFunction<ObjectIntPair<T>>()
                {
                    public int intValueOf(ObjectIntPair<T> item)
                    {
                        return item.getTwo();
                    }
                },
                Lists.fixedSize.<ObjectIntPair<T>>empty()
        ).toImmutable();
    }

    public <V> ImmutableMap<V, T> groupByUniqueKey(Function<? super T, ? extends V> function)
    {
        return this.groupByUniqueKey(function, UnifiedMap.<V, T>newMap()).toImmutable();
    }

    public RichIterable<RichIterable<T>> chunk(int size)
    {
        if (size <= 0)
        {
            throw new IllegalArgumentException("Size for groups must be positive but was: " + size);
        }

        Iterator<T> iterator = this.iterator();
        MutableList<RichIterable<T>> result = Lists.mutable.empty();
        while (iterator.hasNext())
        {
            MutableCollection<T> batch = Bags.mutable.empty();
            for (int i = 0; i < size && iterator.hasNext(); i++)
            {
                batch.add(iterator.next());
            }
            result.add(batch.toImmutable());
        }
        return result.toImmutable();
    }
}