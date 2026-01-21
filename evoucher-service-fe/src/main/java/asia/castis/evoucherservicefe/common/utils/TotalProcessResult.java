package asia.castis.evoucherservicefe.common.utils;

import asia.castis.evoucherservicefe.common.enums.EnumProcessResult;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ToString
public class TotalProcessResult<T, Q> {
    private int successCount;
    private int failCount;
    private int invalidCount;
    private int totalCount;
    private Map<T, ProcessResult<Q>> detailMap;

    public TotalProcessResult() {
        successCount = 0;
        failCount = 0;
        totalCount = 0;
        detailMap = new HashMap<>();
    }

    public void addInvalidResult(T key, String invalidMessage) {
        ProcessResult<Q> processResult = new ProcessResult<>();
        processResult.invalid(invalidMessage);
        invalidCount++;
        totalCount++;
        detailMap.put(key, processResult);
    }

    public void addFailResult(T key, String failMessage) {
        ProcessResult<Q> processResult = new ProcessResult<>();
        processResult.fail(failMessage);
        failCount++;
        totalCount++;
        detailMap.put(key, processResult);
    }

    public void addSuccessResult(T key, Q body) {
        ProcessResult<Q> processResult = new ProcessResult<>();
        processResult.success(body);
        successCount++;
        totalCount++;
        detailMap.put(key, processResult);
    }

    public boolean isAllSuccess() {
        return totalCount == successCount;
    }

    public boolean isSomeSuccess() {
        return successCount > 0;
    }

    public List<Q> successList() {
        return detailMap.values().stream().
                filter(result -> result.getResult() == EnumProcessResult.SUCCESS)
                .map(ProcessResult::getOutput).collect(Collectors.toList());
    }

    public List<Q> failList() {
        return detailMap.values().stream().
                filter(result -> result.getResult() == EnumProcessResult.FAILED)
                .map(ProcessResult::getOutput).collect(Collectors.toList());
    }

    public List<Q> invalidList() {
        return detailMap.values().stream().
                filter(result -> result.getResult() == EnumProcessResult.INVALID)
                .map(ProcessResult::getOutput).collect(Collectors.toList());
    }

    public List<Q> failOrInvalidList() {
        return detailMap.values().stream().
                filter(result -> result.getResult() == EnumProcessResult.FAILED
                        || result.getResult() == EnumProcessResult.INVALID)
                .map(ProcessResult::getOutput).collect(Collectors.toList());
    }

    public boolean isAllFail() {
        return totalCount == failCount;
    }

    public boolean isAllInvalid() {
        return totalCount == invalidCount;
    }

    public boolean isAllFailOrInvalid() {
        return totalCount == (invalidCount + failCount);
    }

    public int getSuccessCount() {
        return successCount;
    }

    public int getFailCount() {
        return failCount;
    }

    public int getInvalidCount() {
        return invalidCount;
    }

    public int getFailOrInvalidCount() {
        return invalidCount + failCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public Map<T, ProcessResult<Q>> getDetailMap() {
        return detailMap;
    }

    public List<Q> getSuccessOutputs() {
        return detailMap
                .values()
                .stream()
                .filter(qProcessResult -> qProcessResult.getResult() == EnumProcessResult.SUCCESS)
                .map(qProcessResult -> qProcessResult.getOutput())
                .collect(Collectors.toList());
    }

    public List<T> getFailOrInvalidInputs() {
        return detailMap
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().getResult() == EnumProcessResult.FAILED || entry.getValue().getResult() == EnumProcessResult.INVALID)
                .map(qProcessResult -> qProcessResult.getKey())
                .collect(Collectors.toList());
    }

    public List<Q> getOutputs() {
        return detailMap
                .values()
                .stream()
                .map(qProcessResult -> qProcessResult.getOutput())
                .collect(Collectors.toList());
    }

    public String report() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("Invalid=%d/%d. ", getInvalidCount(), getTotalCount()));
        builder.append(String.format("Failed=%d/%d. ", getFailCount(), getTotalCount()));
        builder.append(String.format("Success=%d/%d.", getSuccessCount(), getTotalCount()));
        return builder.toString();
    }
}
