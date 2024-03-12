#include <bits/stdc++.h>
using namespace std;
#define M 1000000007
#define ll long long
const int N = 1e5 + 10;
#define ull unsigned long
unsigned long long int fastspcexp(unsigned long long int n)
{
    if (n == 0)
        return 1;
    if (n % 2 == 0)
        return (((fastspcexp(n / 2)) * (fastspcexp(n / 2))) % 1000000007);
    else
        return ((((fastspcexp(n / 2)) * (fastspcexp(n / 2)) * 2) % 1000000007));
}
vector<ull> fact(ull n)
{
    vector<ull> res;
    res.push_back(1);
    for (int i = 2; i <= n; i++)
    {
        ull carry = 0;
        for (int j = 0; j < res.size(); j++)
        {
            ull val = res[j] * i + carry;
            res[j] = val % 10;
            carry = val / 10;
        }
        while (carry != 0)
        {
            res.push_back(carry % 10);
            carry /= 10;
        }
    }
    reverse(res.begin(), res.end());
    return res;
}
bool sortbyCond(const pair<int, int> &a,
                const pair<int, int> &b)
{
    if (a.first != b.first)
        return (a.first > b.first);
    else
        return (a.second < b.second);
}
bool isPrime(int n)
{
    vector<bool> prime(N, 1);
    prime[0] = prime[1] = 0;
    for (int i = 2; i < n; i++)
    {
        if (prime[i] == true)
            for (int j = 2 * i; j < N; j += i)
            {
                prime[j] = false;
            }
    }
    if (prime[n])
        return true;
    else
        return false;
}
vector<vector<ll>> subsetsgeneration(vector<ll> &inp, ll &n)
{
    vector<vector<ll>> subsets;
    ll subset_ct = (1 << n);
    for (int mask = 0; mask < subset_ct; mask++)
    {
        vector<ll> subset;
        for (int i = 0; i < n; i++)
        {
            if ((mask & (1 << i)) != 0)
                subset.push_back(inp[i]);
        }
        subsets.push_back(subset);
    }
    return subsets;
}
// possible sum of all subset algo is
//     ll ans = 1;
//     for (int i = 0; i < n && inp[i] <= ans; i++)
//         ans = ans + inp[i];
//         iska matlab ye hai ki kisi particular moment pe ans-1 tak sare sum bana sakte hai
ll power(ll x, ll n)
{
    ll res = 1;
    while (n > 0)
    {
        if (n & 1)
        {
            res *= x;
            res %= M;
        }
        n >>= 1;
        x *= x;
        x %= M;
    }
    return res % M;
}
// use this if the value of M is also very large i.e; 1e18 types
ll binarymultiply(ll a, ll b)
{
    ll ans = 0;
    while (b > 0)
    {
        if (b & 1)
            ans = (ans + a) % M;
        b >>= 1;
        a += a % M;
    }
    return ans;
}
// set a i'th bit  then or the given number with (1<<i) and to unset it again and it with complement of ~(1<<i)
//  and to toggle the bit xor it with (1<<i)
// a+b = a^b + 2*(a&b)
// from multiple of 4 to next 4 cons numbers xor would be zero
void binary(ll num)
{
    for (int i = 31; i >= 0; i--)
    {
        cout << ((num >> i) & 1);
    }
}
vector<int> permlarg(int k, vector<int> arr)
{
    int n = arr.size();
    unordered_map<int, int> index_map;

    for (int i = 0; i < n; ++i)
    {
        index_map[arr[i]] = i;
    }

    for (int i = 0; i < n && k > 0; ++i)
    {
        if (arr[i] != n - i)
        {
            int current_max = arr[i];
            int index_of_max = index_map[n - i];

            index_map[current_max] = index_of_max;
            index_map[n - i] = i;

            swap(arr[i], arr[index_of_max]);
            --k;
        }
    }

    return arr;
}
int main()
{
    ios_base::sync_with_stdio(false);
    cin.tie(NULL);

    int n, k;
    cin >> n >> k;

    vector<int> arr(n);
    for (int i = 0; i < n; ++i)
    {
        cin >> arr[i];
    }

    vector<int> result = permlarg(k, arr);

    for (int i = 0; i < n; ++i)
    {
        cout << result[i] << " ";
    }
    return 0;
}
