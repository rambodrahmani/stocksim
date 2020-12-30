/*
 * Apache Cassandra Stocksim CQL analytic functions.
 *
 * author: Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */

CREATE OR REPLACE FUNCTION Diff ( p1 float , p2 float)
RETURNS NULL ON NULL INPUT 
RETURNS float 
LANGUAGE java AS '
return p1-p2;'
 ;

 CREATE OR REPLACE FUNCTION Plus( p1 float , p2 float)
RETURNS NULL ON NULL INPUT 
RETURNS float 
LANGUAGE java AS '
return p1+p2;'
 ;

 CREATE OR REPLACE FUNCTION Mul ( p1 float , p2 float)
RETURNS NULL ON NULL INPUT 
RETURNS float 
LANGUAGE java AS '
return p1*p2;'
 ;

  CREATE OR REPLACE FUNCTION Div ( p1 float , p2 float)
RETURNS NULL ON NULL INPUT 
RETURNS float 
LANGUAGE java AS '
return p1/p2;'
 ;