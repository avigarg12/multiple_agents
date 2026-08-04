#!/bin/bash

# 1. Log in to get a token (Save the token in a variable)
echo "Logging in..."
LOGIN_RESPONSE=$(curl -s -X POST http://localhost:8080/api/auth/login -u admin:password)

# Extract token using simple grep/sed parsing (since jq might not be installed)
TOKEN=$(echo $LOGIN_RESPONSE | grep -o '"token":"[^"]*' | grep -o '[^"]*$')

if [ -z "$TOKEN" ]; then
    echo "Login failed! Response was: $LOGIN_RESPONSE"
    exit 1
fi

echo "Acquired Token: $TOKEN"
echo "Firing 20 concurrent requests..."

# Start timer
START_TIME=$(date +%s)

# 2. Fire 20 concurrent requests in the background
for i in {1..20}
do
   # The '&' at the end pushes the curl command to the background
   curl -s -o /dev/null -w "Request #$i - HTTP Status: %{http_code}\n" \
        -X POST http://localhost:8080/api/agents/process \
        -H "Content-Type: application/json" \
        -H "Authorization: Bearer $TOKEN" \
        -d '{"taskType": "HISTORY", "message": "Message '$i'"}' &
done

# 3. Wait for all background processes to finish
wait

END_TIME=$(date +%s)
ELAPSED=$((END_TIME - START_TIME))

echo "All requests finished in $ELAPSED seconds."