// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package edu.uci.ics.amber.engine.architecture.worker.statistics

@SerialVersionUID(0L)
final case class WorkerMetrics(
    workerState: edu.uci.ics.amber.engine.architecture.worker.statistics.WorkerState,
    workerStatistics: edu.uci.ics.amber.engine.architecture.worker.statistics.WorkerStatistics,
    workerInternalState: _root_.scala.Predef.String
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[WorkerMetrics] {
    @transient
    private[this] var __serializedSizeCachedValue: _root_.scala.Int = 0
    private[this] def __computeSerializedValue(): _root_.scala.Int = {
      var __size = 0
      
      {
        val __value = workerState.value
        if (__value != 0) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeEnumSize(1, __value)
        }
      };
      
      {
        val __value = workerStatistics
        if (__value != edu.uci.ics.amber.engine.architecture.worker.statistics.WorkerStatistics.defaultInstance) {
          __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(__value.serializedSize) + __value.serializedSize
        }
      };
      
      {
        val __value = workerInternalState
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(3, __value)
        }
      };
      __size
    }
    override def serializedSize: _root_.scala.Int = {
      var read = __serializedSizeCachedValue
      if (read == 0) {
        read = __computeSerializedValue()
        __serializedSizeCachedValue = read
      }
      read
    }
    def writeTo(`_output__`: _root_.com.google.protobuf.CodedOutputStream): _root_.scala.Unit = {
      {
        val __v = workerState.value
        if (__v != 0) {
          _output__.writeEnum(1, __v)
        }
      };
      {
        val __v = workerStatistics
        if (__v != edu.uci.ics.amber.engine.architecture.worker.statistics.WorkerStatistics.defaultInstance) {
          _output__.writeTag(2, 2)
          _output__.writeUInt32NoTag(__v.serializedSize)
          __v.writeTo(_output__)
        }
      };
      {
        val __v = workerInternalState
        if (!__v.isEmpty) {
          _output__.writeString(3, __v)
        }
      };
    }
    def withWorkerState(__v: edu.uci.ics.amber.engine.architecture.worker.statistics.WorkerState): WorkerMetrics = copy(workerState = __v)
    def withWorkerStatistics(__v: edu.uci.ics.amber.engine.architecture.worker.statistics.WorkerStatistics): WorkerMetrics = copy(workerStatistics = __v)
    def withWorkerInternalState(__v: _root_.scala.Predef.String): WorkerMetrics = copy(workerInternalState = __v)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => {
          val __t = workerState.javaValueDescriptor
          if (__t.getNumber() != 0) __t else null
        }
        case 2 => {
          val __t = workerStatistics
          if (__t != edu.uci.ics.amber.engine.architecture.worker.statistics.WorkerStatistics.defaultInstance) __t else null
        }
        case 3 => {
          val __t = workerInternalState
          if (__t != "") __t else null
        }
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PEnum(workerState.scalaValueDescriptor)
        case 2 => workerStatistics.toPMessage
        case 3 => _root_.scalapb.descriptors.PString(workerInternalState)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToSingleLineUnicodeString(this)
    def companion = edu.uci.ics.amber.engine.architecture.worker.statistics.WorkerMetrics
    // @@protoc_insertion_point(GeneratedMessage[edu.uci.ics.amber.engine.architecture.worker.WorkerMetrics])
}

object WorkerMetrics extends scalapb.GeneratedMessageCompanion[edu.uci.ics.amber.engine.architecture.worker.statistics.WorkerMetrics] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[edu.uci.ics.amber.engine.architecture.worker.statistics.WorkerMetrics] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): edu.uci.ics.amber.engine.architecture.worker.statistics.WorkerMetrics = {
    var __workerState: edu.uci.ics.amber.engine.architecture.worker.statistics.WorkerState = edu.uci.ics.amber.engine.architecture.worker.statistics.WorkerState.UNINITIALIZED
    var __workerStatistics: _root_.scala.Option[edu.uci.ics.amber.engine.architecture.worker.statistics.WorkerStatistics] = _root_.scala.None
    var __workerInternalState: _root_.scala.Predef.String = ""
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 8 =>
          __workerState = edu.uci.ics.amber.engine.architecture.worker.statistics.WorkerState.fromValue(_input__.readEnum())
        case 18 =>
          __workerStatistics = _root_.scala.Some(__workerStatistics.fold(_root_.scalapb.LiteParser.readMessage[edu.uci.ics.amber.engine.architecture.worker.statistics.WorkerStatistics](_input__))(_root_.scalapb.LiteParser.readMessage(_input__, _)))
        case 26 =>
          __workerInternalState = _input__.readStringRequireUtf8()
        case tag => _input__.skipField(tag)
      }
    }
    edu.uci.ics.amber.engine.architecture.worker.statistics.WorkerMetrics(
        workerState = __workerState,
        workerStatistics = __workerStatistics.getOrElse(edu.uci.ics.amber.engine.architecture.worker.statistics.WorkerStatistics.defaultInstance),
        workerInternalState = __workerInternalState
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[edu.uci.ics.amber.engine.architecture.worker.statistics.WorkerMetrics] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      edu.uci.ics.amber.engine.architecture.worker.statistics.WorkerMetrics(
        workerState = edu.uci.ics.amber.engine.architecture.worker.statistics.WorkerState.fromValue(__fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).map(_.as[_root_.scalapb.descriptors.EnumValueDescriptor]).getOrElse(edu.uci.ics.amber.engine.architecture.worker.statistics.WorkerState.UNINITIALIZED.scalaValueDescriptor).number),
        workerStatistics = __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).map(_.as[edu.uci.ics.amber.engine.architecture.worker.statistics.WorkerStatistics]).getOrElse(edu.uci.ics.amber.engine.architecture.worker.statistics.WorkerStatistics.defaultInstance),
        workerInternalState = __fieldsMap.get(scalaDescriptor.findFieldByNumber(3).get).map(_.as[_root_.scala.Predef.String]).getOrElse("")
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = StatisticsProto.javaDescriptor.getMessageTypes().get(2)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = StatisticsProto.scalaDescriptor.messages(2)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 2 => __out = edu.uci.ics.amber.engine.architecture.worker.statistics.WorkerStatistics
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = {
    (__fieldNumber: @_root_.scala.unchecked) match {
      case 1 => edu.uci.ics.amber.engine.architecture.worker.statistics.WorkerState
    }
  }
  lazy val defaultInstance = edu.uci.ics.amber.engine.architecture.worker.statistics.WorkerMetrics(
    workerState = edu.uci.ics.amber.engine.architecture.worker.statistics.WorkerState.UNINITIALIZED,
    workerStatistics = edu.uci.ics.amber.engine.architecture.worker.statistics.WorkerStatistics.defaultInstance,
    workerInternalState = ""
  )
  implicit class WorkerMetricsLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, edu.uci.ics.amber.engine.architecture.worker.statistics.WorkerMetrics]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, edu.uci.ics.amber.engine.architecture.worker.statistics.WorkerMetrics](_l) {
    def workerState: _root_.scalapb.lenses.Lens[UpperPB, edu.uci.ics.amber.engine.architecture.worker.statistics.WorkerState] = field(_.workerState)((c_, f_) => c_.copy(workerState = f_))
    def workerStatistics: _root_.scalapb.lenses.Lens[UpperPB, edu.uci.ics.amber.engine.architecture.worker.statistics.WorkerStatistics] = field(_.workerStatistics)((c_, f_) => c_.copy(workerStatistics = f_))
    def workerInternalState: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.workerInternalState)((c_, f_) => c_.copy(workerInternalState = f_))
  }
  final val WORKER_STATE_FIELD_NUMBER = 1
  final val WORKER_STATISTICS_FIELD_NUMBER = 2
  final val WORKER_INTERNAL_STATE_FIELD_NUMBER = 3
  def of(
    workerState: edu.uci.ics.amber.engine.architecture.worker.statistics.WorkerState,
    workerStatistics: edu.uci.ics.amber.engine.architecture.worker.statistics.WorkerStatistics,
    workerInternalState: _root_.scala.Predef.String
  ): _root_.edu.uci.ics.amber.engine.architecture.worker.statistics.WorkerMetrics = _root_.edu.uci.ics.amber.engine.architecture.worker.statistics.WorkerMetrics(
    workerState,
    workerStatistics,
    workerInternalState
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[edu.uci.ics.amber.engine.architecture.worker.WorkerMetrics])
}
