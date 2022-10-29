package onboarding;

import java.util.*;

public class Problem7 {
    public static final int FRIEND_POINT = 20;
    public static final int ZERO_POINT = 0;
    public static final int ONE_POINT = 1;
    public static List<String> userFriend;
    public static Map<String, Integer> sameFriend;
    public static List<String> result;
    public static List<String> samePointList;
    public static String currentUser;
    public static final int MAX_FRIEND_RECOMMEND = 5;
    public static final int INIT_ZERO = 0;

    public static List<String> solution(String user, List<List<String>> friends, List<String> visitors) {
        init(user);

        findUserFriend(friends);
        findSameFriend(friends);
        findVisitor(visitors);

        List<String> answer = makeOrder();
        return answer;
    }

    public static void init(String user) {
        currentUser = user;
        userFriend = new ArrayList<>();
        sameFriend = new HashMap<>();
        result = new ArrayList<>();
        samePointList = new ArrayList<>();
    }

    public static List<String> orderSamePointFriend(List<Map.Entry<String, Integer>> listEntries) {
        int count = INIT_ZERO;

        for (int targetIndex = INIT_ZERO; targetIndex < listEntries.size(); targetIndex++) {
            int targetPoint = listEntries.get(targetIndex).getValue();

            if (count >= MAX_FRIEND_RECOMMEND) {
                return result;
            }

            checkSamePointFriend(listEntries, targetIndex, targetPoint);
            targetIndex = (samePointList.isEmpty() ? empty(listEntries, count, targetIndex): notEmpty(listEntries, targetIndex, count));
        }
        return result;
    }

    private static int empty(List<Map.Entry<String, Integer>> listEntries, int count, int targetIndex) {
        result.add(listEntries.get(targetIndex).getKey());
        count++;
        return targetIndex;
    }

    public static int notEmpty(List<Map.Entry<String, Integer>> listEntries, int targetIndex, int count) {
        samePointList.add(listEntries.get(targetIndex).getKey());
        Collections.sort(samePointList);

        for (int samePointListIndex = INIT_ZERO; samePointListIndex < samePointList.size(); samePointListIndex++) {
            result.add(samePointList.get(samePointListIndex));
            count++;
        }
        targetIndex += (samePointList.size() - 1);
        samePointList.clear();

        return targetIndex;
    }

    public static void checkSamePointFriend(List<Map.Entry<String, Integer>> listEntries, int targetIndex, int targetPoint) {
        for (int compareIndex = targetIndex + 1; compareIndex < listEntries.size(); compareIndex++) {
            if (listEntries.get(compareIndex).getValue() == targetPoint) {
                samePointList.add(listEntries.get(compareIndex).getKey());
            }
        }
    }

    public static List<String> makeOrder() {
        List<Map.Entry<String, Integer>> listEntries = new ArrayList<Map.Entry<String, Integer>>(sameFriend.entrySet());

        Collections.sort(listEntries, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> obj1, Map.Entry<String, Integer> obj2) {
                return obj2.getValue().compareTo(obj1.getValue());
            }
        });

        return orderSamePointFriend(listEntries);
    }

    public static void findVisitor(List<String> visitors) {
        for (String visitor : visitors) {

            if (!isSameWithUserFriend(visitor)) {
                checkExistName(visitor);
                addName(visitor, ONE_POINT);
            }
        }
    }

    public static void findSameFriend(List<List<String>> friends) {
        for (List<String> friendList : friends) {
            checkUserFriends(friendList);
        }
    }

    //user와 겹치는 친구 있는지 체크하고 있으면 sameFriend에 넣음
    public static void checkUserFriends(List<String> friendList) {
        for (String name : friendList) {
            if (!isSameWithUser(name) && !isSameWithUserFriend(name)) {
                checkExistName(name);
                addName(name, FRIEND_POINT);
            }
        }
    }

    public static void addName(String name, int point) {
        sameFriend.put(name, sameFriend.get(name) + point);
    }

    public static void checkExistName(String name) {
        if (!isContainName(name)) {
            sameFriend.put(name, ZERO_POINT);
        }
    }

    public static boolean isContainName(String name) {
        return sameFriend.containsKey(name);
    }

    public static boolean isSameWithUserFriend(String name) {
        return userFriend.contains(name);
    }

    public static boolean isSameWithUser(String name) {
        return name.equals(currentUser);
    }

    public static void findUserFriend(List<List<String>> friends) {
        for (List<String> friendList : friends) {
            for (String friend : friendList) {
                checkUserFriend(friend, friendList);
            }
        }
    }

    public static void checkUserFriend(String friend, List<String> friendList) {
        if (isUserFriend(friend, friendList)) {
            userFriend.add(friend);
        }
    }

    public static boolean isUserFriend(String friend, List<String> friendList) {
        return (friendList.contains(currentUser) && !friend.equals(currentUser));
    }
}