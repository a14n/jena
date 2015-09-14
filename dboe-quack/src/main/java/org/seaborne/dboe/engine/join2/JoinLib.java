/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  See the NOTICE file distributed with this work for additional
 *  information regarding copyright ownership.
 */

package org.seaborne.dboe.engine.join2;

import org.apache.jena.graph.Node ;
import org.apache.jena.sparql.core.Var ;
import org.apache.jena.sparql.engine.binding.Binding ;
import org.seaborne.dboe.engine.JoinKey ;
import org.seaborne.dboe.engine.Row ;
import org.seaborne.dboe.engine.join.Hasher ;

/** Internal operations in support of join algorithms. */
class JoinLib {

    /** Control stats output / development use */ 
    static final boolean JOIN_EXPLAIN = false;

    // No hash key marker.
    public static final Object noKeyHash = new Object() ;
    public static final long nullHashCode = 5 ;

    public static long hash(Var v, Node x) {
        long h = 17;
        if ( v != null )
            h = h ^ v.hashCode();
        if ( x != null )
            h = h ^ x.hashCode();
        return h;
    }
    
    public static <X> Hasher<X> hash() { 
        return new Hasher<X>(){ 
            @Override 
            public long hash(Var v, X x) 
            { 
                long h = 17 ;
                if ( v != null )
                    h = h ^ v.hashCode() ;
                if ( x != null )  
                    h = h ^ x.hashCode() ;
                return h ;
            }
        } ;
    }

    public static Object hash(JoinKey joinKey, Binding row) {
        long x = 31 ;
        boolean seenJoinKeyVar = false ; 
        // Neutral to order in the set.
        for ( Var v : joinKey ) {
            Node value = row.get(v) ;
            long h = nullHashCode ;
            if ( value != null ) {
                seenJoinKeyVar = true ;
                h = hash(v, value) ;
            } else {
                // In join key, not in row.
            }
                
            x = x ^ h ;
        }
        if ( ! seenJoinKeyVar )
            return noKeyHash ;
        return x ;
    }
    
    public static <X> Object hash(Hasher<X> hasher, JoinKey joinKey, Row<X> row) {
        long x = 31 ;
        boolean seenJoinKeyVar = false ; 
        // Neutral to order in the set.
        for ( Var v : joinKey ) {
            X value = row.get(v) ;
            long h = nullHashCode ;
            if ( value != null ) {
                seenJoinKeyVar = true ;
                h = hasher.hash(v, value) ;
            } else {
                // In join key, not in row.
            }

            x = x ^ h ;
        }
        if ( ! seenJoinKeyVar )
            return noKeyHash ;
        return x ;
    }
}

