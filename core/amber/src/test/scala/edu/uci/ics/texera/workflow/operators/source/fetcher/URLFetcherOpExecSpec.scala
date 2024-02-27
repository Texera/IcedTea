package edu.uci.ics.texera.workflow.operators.source.fetcher

import edu.uci.ics.texera.workflow.common.tuple.schema.Schema
import org.scalatest.BeforeAndAfter
import org.scalatest.flatspec.AnyFlatSpec

class URLFetcherOpExecSpec extends AnyFlatSpec with BeforeAndAfter {

  val resultSchema: Schema = new URLFetcherOpDesc().sourceSchema()

  it should "fetch url and output one tuple with raw bytes" in {
    val fetcherOpExec = new URLFetcherOpExec("https://www.google.com", DecodingMethod.RAW_BYTES)
    val iterator = fetcherOpExec.produceTuple()
    assert(iterator.next().fields.toList.head.isInstanceOf[Array[Byte]])
    assert(!iterator.hasNext)
  }

  it should "fetch url and output one tuple with UTF-8 string" in {
    val fetcherOpExec = new URLFetcherOpExec("https://www.google.com", DecodingMethod.UTF_8)
    val iterator = fetcherOpExec.produceTuple()
    assert(iterator.next().fields.toList.head.isInstanceOf[String])
    assert(!iterator.hasNext)
  }

}
