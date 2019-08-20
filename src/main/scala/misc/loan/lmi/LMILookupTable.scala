package misc.loan.lmi

case class Range(start: Long, end: Long) {
  def withinRange(value: Long): Boolean = value <= end && value >= start
}
case class TableRow(loanPercentage: Int, columns: Seq[(Range, Double)]) {
  val rangeMap = columns.toMap
  def findInRange(capital: Long): Double = {
    rangeMap.find(tuple => tuple._1.withinRange(capital)).getOrElse((Range(-1L, 0L), 0d))._2
  }
}
class LMILookupTable(val rows: Seq[TableRow]) {
  val rowMap = rows.map(row => (row.loanPercentage, row)).toMap
  def lookupLMIFor(capital: Long, percentLoan: Int) = rowMap.get(percentLoan).get.findInRange(capital)
}
object LMILookupTable {
  def apply(header: Array[String], data: Array[String]): LMILookupTable = {
    val headerDataMap = header zip data toMap
    val rangeValueMap = headerDataMap.tail.map(tuple => {
      val start = tuple._1.split("-")(0).trim.toLong
      val end = tuple._1.split("-")(1).trim.toLong
      (Range(start, end), tuple._2.toDouble)
    }).toSeq
    val tableRow = TableRow(headerDataMap.head._2.toInt, rangeValueMap)
    new LMILookupTable(Seq(tableRow))
  }
  def apply(header: Array[String], data: Array[String], table: LMILookupTable): LMILookupTable = {
    new LMILookupTable(table.rows ++ apply(header, data).rows)
  }
  def apply(fileContent: Array[String]): LMILookupTable = {
    val header = fileContent.head.split(",", -1)
    val dataRows = fileContent.tail
    val funcToReduce = (table: LMILookupTable, dataItems: Array[String]) => LMILookupTable(header, dataItems, table)
    dataRows.map(str => str.split(",", -1)).foldLeft(new LMILookupTable(Seq.empty))(funcToReduce)
  }
}
