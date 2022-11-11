package ar.edu.unq.desapp.grupog.criptop2p.utils.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Aspect
@Component
@Slf4j
public class LogExecutionTimeAspectAnnotation {

    @Around("@annotation(LogExecutionTime) || @within(LogExecutionTime)")
    public Object logExecutionTimeAnnotation(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;

        CodeSignature methodSignature = (CodeSignature) joinPoint.getSignature();

        List<Object> parameters = List.of(methodSignature.getParameterNames());
        List<Object> argumentsProvided = List.of(joinPoint.getArgs());

        Map<Object, Object> paramsAndArgs = IntStream
                .range(0, parameters.size())
                .boxed()
                .collect(Collectors.toMap(parameters::get, argumentsProvided::get));

        log.info(joinPoint.getSignature().toLongString() + " executed with args " + paramsAndArgs + " finalized in " + executionTime + " ms");

        return proceed;
    }

}
