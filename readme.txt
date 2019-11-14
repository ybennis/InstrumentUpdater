How To RUN:
1- In target folder, you will find InstrumentMergerApp-0.0.1-SNAPSHOT-jar-with-dependencies jar file
2- Run the following command : java -jar InstrumentMergerApp-0.0.1-SNAPSHOT-jar-with-dependencies
3- JDK 1.8 and Maven are technologies being used to build the jar file 
4- Run the following command to produce the jar :  mvn clean package
5- The main class will execute the merging of the two instruments as stated in the requirements
 


Other Considerations :
1- InstrumentPublisherEngine uses a singleton pattern but offers a flexible way to scale 
number of workers by using an Executor Service Objects relying on the number of processors
2- For the sake of the demonstration, the main class TradingInstrumentUpdater illustrates how to dynamically inject 
different rules into the different Sources. 
3- Supporting a new Instrument source like LME or PRIME consists of the following steps :
   a- Add a new type with SourceType Enum Class
   b- Extending the TradingSource abstract class and implementing necessary contracts
   c- Update TradingSourceFactory to include the newly integrated source 
Ideally, we could have implemented a more dynamic way to inject a Trading Source on the fly through
TradingSourceFactory , the same way used to inject rules. For the current version, the trading sources are already predefined
4- The InstrumentPublisherEngine being the controller entry point for consumming instruments is designed to run in a scheduled 
manner definetely 