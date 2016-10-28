#Preface
This is a exercise I have done by using Scala with Play framework.  
The main function of the application is an address book which collects legistrator's contact information and metadata in Taiwan. Further more, it provide convenient ways to access the information.

#Project information and contributors
see http://beta.hackfoldr.org/callliwei

#Enviroment
java 1.8.0_92  
scala 2.11.8  
sbt 0.13.11  
activator 1.3.10  
play 2.5.8

#Framework and components
play  
anorm  
h2  
bootstrap  
GA  
see build.sbt in details.

#sitemap
/sitemap.xml

#API
/api/v1
/api (always pointing to the newest version, suck as /api/v1)
/api/profile/{legistrator-name}
/api/list
/api/list?q={query-string}
/api/list?q=黃國
/api/list/district/{district-name}
/api/list/district/台北市
/api/list/profession/{profession-name}
/api/list/profession/交通
/api/list/party/{party-name}
/api/list/party/時代力量
/api/category/{category-name}
/api/category/party
/api/category/profession
/api/category/district


#Installation
TODO

#Run it
TODO

#Dataset
data/t9a/h2.csv is the newest dataset used in system.  
alternatively, you can fetch all dataset in json on /api/list  
data/README.md for more information.

#System architecture
##database
h2 database
##Backend
scala
play
anorm
##Brontend
bootstrap

