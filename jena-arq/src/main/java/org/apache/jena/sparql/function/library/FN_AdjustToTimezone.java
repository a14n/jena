/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.jena.sparql.function.library;

import java.util.List;

import org.apache.jena.atlas.lib.Lib;
import org.apache.jena.query.QueryBuildException;
import org.apache.jena.sparql.expr.ExprEvalException;
import org.apache.jena.sparql.expr.ExprList;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.expr.nodevalue.XSDFuncOp;
import org.apache.jena.sparql.function.FunctionBase;

/** Do any of FN_Adjust(date/time)ToTimezone */
public class FN_AdjustToTimezone extends FunctionBase {
    public FN_AdjustToTimezone(){super();}

    @Override
    public void checkBuild(String uri, ExprList args)
    {
        if ( args.size() != 1 && args.size() != 2 )
            throw new QueryBuildException("Function '"+ Lib.className(this)+"' takes one or two arguments") ;
    }
    @Override
    public NodeValue exec(List<NodeValue> args)
    {
        if ( args.size() != 1 && args.size() != 2 )
            throw new ExprEvalException("fn:adjust-to-timezone: Wrong number of arguments: "+args.size()+" : [wanted 1 or 2]") ;

        NodeValue v1 = args.get(0) ;
        if ( !v1.isDateTime() && !v1.isDate() && !v1.isTime() )
            throw new ExprEvalException("Not an xsd:dateTime, xsd:date or xsd:time : " + v1);

        if ( args.size() == 2 ) {
            NodeValue v2 = args.get(1) ;
            return XSDFuncOp.adjustToTimezone(v1, v2) ;
        }

        return XSDFuncOp.adjustToTimezone(v1, null) ;
    }
}
