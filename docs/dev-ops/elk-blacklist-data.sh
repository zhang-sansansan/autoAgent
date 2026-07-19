#!/bin/bash

# 妯℃嫙鍚慐lasticsearch鍐欏叆鎷煎洟椤圭洰榛戝悕鍗曢檺娴佹暟鎹殑鑴氭湰
# 浣跨敤鏂规硶: ./elk-blacklist-data.sh

# Elasticsearch閰嶇疆
ES_HOST="localhost:9200"
INDEX_NAME="group-buy-market-log-$(date +%Y.%m.%d)"

# 棰滆壊杈撳嚭
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}寮€濮嬪悜Elasticsearch妯℃嫙鍐欏叆鎷煎洟椤圭洰榛戝悕鍗曢檺娴佹暟鎹?..${NC}"
echo -e "${YELLOW}鐩爣绱㈠紩: ${INDEX_NAME}${NC}"
echo -e "${YELLOW}ES鍦板潃: ${ES_HOST}${NC}"

# 妫€鏌lasticsearch杩炴帴
echo "妫€鏌lasticsearch杩炴帴..."
if ! curl -s "http://${ES_HOST}/_cluster/health" > /dev/null; then
    echo -e "${RED}閿欒: 鏃犳硶杩炴帴鍒癊lasticsearch (${ES_HOST})${NC}"
    echo "璇风‘淇滶lasticsearch鏈嶅姟姝ｅ湪杩愯"
    exit 1
fi
echo -e "${GREEN}Elasticsearch杩炴帴姝ｅ父${NC}"

# 妯℃嫙鐢ㄦ埛鏁版嵁
USERS=("user001" "user002" "user003" "user004" "user005" "user006" "user007" "user008" "user009" "user010")
IPS=("192.168.1.100" "192.168.1.101" "192.168.1.102" "10.0.0.50" "10.0.0.51" "172.16.0.100" "172.16.0.101" "203.0.113.10" "203.0.113.11" "198.51.100.20")
USER_AGENTS=("Mozilla/5.0 (iPhone; CPU iPhone OS 14_0 like Mac OS X)" "Mozilla/5.0 (Android 10; Mobile)" "Mozilla/5.0 (Windows NT 10.0; Win64; x64)" "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7)")
GROUP_BUY_PRODUCTS=("product_001" "product_002" "product_003" "product_004" "product_005")
LIMIT_REASONS=("璁块棶棰戠巼杩囬珮" "鎭舵剰鍒峰崟" "寮傚父IP璁块棶" "瓒呰繃姣忔棩闄愬埗" "榛戝悕鍗曠敤鎴?)
LIMIT_TYPES=("rate_limit" "frequency_limit" "ip_blacklist" "daily_limit" "user_blacklist")

# 鐢熸垚闅忔満鏃堕棿鎴筹紙鏈€杩?4灏忔椂鍐咃級
generate_timestamp() {
    local now=$(date +%s)
    local random_offset=$((RANDOM % 86400))  # 24灏忔椂鍐呴殢鏈?    local timestamp=$((now - random_offset))
    date -r $timestamp -Iseconds
}

# 鐢熸垚闅忔満鏃ュ織绾у埆
generate_log_level() {
    local levels=("ERROR" "WARN" "INFO")
    echo "${levels[$((RANDOM % ${#levels[@]}))]}"
}

# 鐢熸垚妯℃嫙闄愭祦鏃ュ織鏁版嵁
generate_log_data() {
    local user_id="${USERS[$((RANDOM % ${#USERS[@]}))]}"
    local ip="${IPS[$((RANDOM % ${#IPS[@]}))]}"
    local user_agent="${USER_AGENTS[$((RANDOM % ${#USER_AGENTS[@]}))]}"
    local product="${GROUP_BUY_PRODUCTS[$((RANDOM % ${#GROUP_BUY_PRODUCTS[@]}))]}"
    local limit_reason="${LIMIT_REASONS[$((RANDOM % ${#LIMIT_REASONS[@]}))]}"
    local limit_type="${LIMIT_TYPES[$((RANDOM % ${#LIMIT_TYPES[@]}))]}"
    local timestamp=$(generate_timestamp)
    local log_level=$(generate_log_level)
    local request_count=$((RANDOM % 100 + 50))  # 50-149娆¤姹?    local limit_threshold=$((RANDOM % 50 + 20))  # 20-69鐨勯檺鍒堕槇鍊?    
    cat << EOF
{
  "@timestamp": "${timestamp}",
  "level": "${log_level}",
  "logger": "com.fuzhengwei.security.RateLimitFilter",
  "thread": "http-nio-8080-exec-$((RANDOM % 10 + 1))",
  "message": "鐢ㄦ埛璁块棶鎷煎洟椤圭洰琚檺娴?- 鐢ㄦ埛ID: ${user_id}, 浜у搧: ${product}, 鍘熷洜: ${limit_reason}, IP: ${ip}, 璇锋眰娆℃暟: ${request_count}, 闄愬埗闃堝€? ${limit_threshold}",
  "application": "group-buy-market",
  "environment": "production",
  "service": "group-buy-service",
  "user_id": "${user_id}",
  "ip_address": "${ip}",
  "user_agent": "${user_agent}",
  "product_id": "${product}",
  "limit_type": "${limit_type}",
  "limit_reason": "${limit_reason}",
  "request_count": ${request_count},
  "limit_threshold": ${limit_threshold},
  "action": "blocked",
  "endpoint": "/api/group-buy/join",
  "method": "POST",
  "status_code": 429,
  "response_time": $((RANDOM % 100 + 10)),
  "session_id": "session_$(date +%s)_${RANDOM}",
  "trace_id": "trace_$(date +%s)_${RANDOM}",
  "tags": ["闄愭祦", "榛戝悕鍗?, "鎷煎洟", "瀹夊叏"]
}
EOF
}

# 鎵归噺鍐欏叆鏁版嵁
echo "寮€濮嬬敓鎴愬苟鍐欏叆妯℃嫙鏁版嵁..."
for i in {1..50}; do
    echo -n "鍐欏叆绗?$i 鏉℃暟鎹?.. "
    
    # 鐢熸垚鏃ュ織鏁版嵁
    log_data=$(generate_log_data)
    
    # 鍐欏叆鍒癊lasticsearch
    response=$(curl -s -X POST "http://${ES_HOST}/${INDEX_NAME}/_doc" \
        -H "Content-Type: application/json" \
        -d "$log_data")
    
    # 妫€鏌ュ啓鍏ョ粨鏋?    if echo "$response" | grep -q '"result":"created"'; then
        echo -e "${GREEN}鎴愬姛${NC}"
    else
        echo -e "${RED}澶辫触${NC}"
        echo "鍝嶅簲: $response"
    fi
    
    # 闅忔満寤惰繜锛屾ā鎷熺湡瀹炲満鏅?    sleep 0.$((RANDOM % 5 + 1))
done

echo -e "${GREEN}鏁版嵁鍐欏叆瀹屾垚锛?{NC}"
echo "鏌ョ湅绱㈠紩淇℃伅:"
curl -s "http://${ES_HOST}/${INDEX_NAME}/_count" | jq .

echo -e "${YELLOW}鍙互浣跨敤浠ヤ笅鍛戒护鏌ョ湅鍐欏叆鐨勬暟鎹?${NC}"
echo "curl -X GET \"http://${ES_HOST}/${INDEX_NAME}/_search?pretty&size=5\""
echo -e "${YELLOW}鎴栬€呭湪Kibana涓煡鐪嬬储寮? ${INDEX_NAME}${NC}"
