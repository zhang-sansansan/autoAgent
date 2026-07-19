#!/bin/bash

# https://cr.console.aliyun.com/cn-hangzhou/instance/credentials

# Ensure the script exits if any command fails
set -e

# Define variables for the registry and image
ALIYUN_REGISTRY="registry.cn-hangzhou.aliyuncs.com"
NAMESPACE="system"
IMAGE_NAME="ai-agent-station-study-ann-app"
IMAGE_TAG="1.0-SNAPSHOT"

# 璇诲彇鏈湴閰嶇疆鏂囦欢
if [ -f ".local-config" ]; then
  source .local-config
else
  echo ".local-config 鏂囦欢涓嶅瓨鍦紝璇峰垱寤哄苟濉啓 ALIYUN_USERNAME 鍜?ALIYUN_PASSWORD"
  exit 1
fi

# Login to Aliyun Docker Registry
echo "Logging into Aliyun Docker Registry..."
docker login --username="${ALIYUN_USERNAME}" --password="${ALIYUN_PASSWORD}" $ALIYUN_REGISTRY

# Tag the Docker image
echo "Tagging the Docker image..."
docker tag ${NAMESPACE}/${IMAGE_NAME}:${IMAGE_TAG} ${ALIYUN_REGISTRY}/${NAMESPACE}/${IMAGE_NAME}:${IMAGE_TAG}

# Push the Docker image to Aliyun
echo "Pushing the Docker image to Aliyun..."
docker push ${ALIYUN_REGISTRY}/${NAMESPACE}/${IMAGE_NAME}:${IMAGE_TAG}

echo "Docker image pushed successfully! "

echo "妫€鍑哄湴鍧€锛歞ocker pull ${ALIYUN_REGISTRY}/${NAMESPACE}/${IMAGE_NAME}:${IMAGE_TAG}"
echo "鏍囩璁剧疆锛歞ocker tag ${ALIYUN_REGISTRY}/${NAMESPACE}/${IMAGE_NAME}:${IMAGE_TAG} ${NAMESPACE}/${IMAGE_NAME}:${IMAGE_TAG}"

# Logout from Aliyun Docker Registry
echo "Logging out from Aliyun Docker Registry..."
docker logout $ALIYUN_REGISTRY

