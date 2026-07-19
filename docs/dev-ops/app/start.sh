CONTAINER_NAME=ai-agent-station-study-ann
IMAGE_NAME=system/ai-agent-station-study-ann:1.0-SNAPSHOT
PORT=8091

echo "瀹瑰櫒閮ㄧ讲寮€濮?${CONTAINER_NAME}"

# 鍋滄瀹瑰櫒
docker stop ${CONTAINER_NAME}

# 鍒犻櫎瀹瑰櫒
docker rm ${CONTAINER_NAME}

# 鍚姩瀹瑰櫒
docker run --name ${CONTAINER_NAME} \
-p ${PORT}:${PORT} \
-d ${IMAGE_NAME}

echo "瀹瑰櫒閮ㄧ讲鎴愬姛 ${CONTAINER_NAME}"

docker logs -f ${CONTAINER_NAME}
