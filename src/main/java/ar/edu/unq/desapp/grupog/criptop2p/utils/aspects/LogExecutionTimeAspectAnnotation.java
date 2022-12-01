package ar.edu.unq.desapp.grupog.criptop2p.utils.aspects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class LogExecutionTimeAspectAnnotation {

    private final ObjectMapper mapper;

    @Around("@annotation(LogExecutionTime) || @within(LogExecutionTime)")
    public Object logExecutionTimeAnnotation(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;

        CodeSignature methodSignature = (CodeSignature) joinPoint.getSignature();

        List<Object> parameters = List.of(methodSignature.getParameterNames());
        List<Object> argumentsProvided = Arrays
                .stream(joinPoint.getArgs())
                .map(arg -> {
                    try {
                        return mapper.writeValueAsString(arg);
                    } catch (JsonProcessingException e) {
                        return arg;
                    }
                })
                .toList();

        Map<Object, Object> paramsAndArgs = IntStream
                .range(0, parameters.size())
                .boxed()
                .collect(Collectors.toMap(parameters::get, argumentsProvided::get));

        String operator = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        log.info("Method execution: " +
                "<user: " + operator + ", " +
                "method: " + methodSignature.toLongString() + ", " +
                "args: " + paramsAndArgs + ", " +
                "execution time: " + executionTime + " ms>");

        return proceed;
    }

}
