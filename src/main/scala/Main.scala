import io.opentelemetry.api.trace.Span
import io.opentelemetry.api.trace.SpanKind
import io.opentelemetry.api.trace.Tracer
import io.opentelemetry.sdk.OpenTelemetrySdk
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor
import io.opentelemetry.sdk.autoconfigure.AutoConfiguredOpenTelemetrySdk
import io.opentelemetry.sdk.trace.SdkTracerProvider
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor
import io.opentelemetry.api.trace.Tracer
import io.opentelemetry.context.Context
import io.opentelemetry.exporter.logging.otlp.OtlpJsonLoggingSpanExporter

// https://opentelemetry.io/docs/languages/java/instrumentation/#tracing-sdk
object Main extends App {
  println("Hello, World!")
  // initTracer()
  test2()
  println("Hello, World!")

  private[this] def test2() = {
    val sdk = AutoConfiguredOpenTelemetrySdk.initialize()
      .getOpenTelemetrySdk();

    val tracerProvider = SdkTracerProvider.builder()
      .addSpanProcessor(SimpleSpanProcessor.builder(OtlpJsonLoggingSpanExporter.create()).build())
      .build();

    val tracer = tracerProvider.get("instrumentation-scope-name", "instrumentation-scope-version")
//    val tracer = sdk.getTracer(
//      "instrumentation-scope-name",
//      "instrumentation-scope-version"
//    )

    val span = tracer.spanBuilder("rollTheDice").startSpan

    val childSpan = tracer.spanBuilder("child2").setParent(Context.current.`with`(span)).startSpan
    childSpan.addEvent("child event")
    childSpan.end()

    span.end()

  }
//
//  private[this] def initTracer() = {
//    val exporter = new LoggingSpanExporter()
//    val spanProcessor = BatchSpanProcessor.builder(exporter).build()
//    val openTelemetry = OpenTelemetrySdk.builder()
//      .setTracerProvider(
//        SdkTracerProvider.builder()
//          .addSpanProcessor(spanProcessor)
//          .build()
//      )
//      .buildAndRegisterGlobal()
//    val tracer = openTelemetry.getTracer("my_tracer")
//    val span: Span = tracer.spanBuilder("my_span").setSpanKind(SpanKind.INTERNAL).startSpan()
//    try {
//      // Your code here
//      Thread.sleep(1000)
//    } finally {
//      span.end()
//    }
//  }
}