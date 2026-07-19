
# 鏅€氶暅鍍忔瀯寤猴紝闅忕郴缁熺増鏈瀯寤?amd/arm
docker build -t system/ai-agent-station-study-ann-app:1.0-SNAPSHOT -f ./Dockerfile .

# 鍏煎 amd銆乤rm 鏋勫缓闀滃儚
# docker buildx build --load --platform liunx/amd64,linux/arm64 -t xiaofuge/xfg-frame-archetype-app:1.0 -f ./Dockerfile . --push
