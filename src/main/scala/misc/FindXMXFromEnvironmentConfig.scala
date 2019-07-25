package misc

import java.io.File

import org.apache.commons.io.FileUtils

import scala.collection.JavaConverters._
import scala.io.Source

object FindXMXFromEnvironmentConfig extends App {
  val directoryToScan = new File("d:/work/puppet/site/sc_tomcat/templates/app/tomcat")
  FileUtils.listFiles(directoryToScan, Array("rb"), true).iterator().asScala.map(file =>
    (file.getParentFile, Source.fromFile(file).getLines().filter(_.contains("-Xmx")).next())
  ).foreach(tuple => println(s"${tuple._1.getName} => ${tuple._2}"))
}
