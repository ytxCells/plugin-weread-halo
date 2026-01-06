
<template>
  <VPageHeader title="微信读书">
    <template #actions>
      <VSpace>
        <VButton :route="{name:'config'}"  size="sm">
          配置
        </VButton>
        <VButton type="secondary" @click="synchronizationWeRead">
          同步数据
        </VButton>
        <VButton type="danger" @click="clearWeRead"> 清空 </VButton>
      </VSpace>
    </template>
  </VPageHeader>
  <div class="m-0 md:m-4">
    <VCard :body-class="['!p-0']">
      <template #header>
      </template>
      <VLoading v-if="isLoading" />
      <Transition v-else-if="!wereadData?.length" appear name="fade">
        <VEmpty
                message="暂无订阅文章记录"
                title="暂无订阅文章记录"
        >
          <template #actions>
            <VSpace>
              <VButton @click="refetch()"> 刷新 </VButton>
            </VSpace>
          </template>
        </VEmpty>
      </Transition>
      <Transition v-else appear name="fade">
        <div class="w-full relative overflow-x-auto">
          <table class="w-full  text-sm text-left text-gray-500 widefat ">
            <thead class="text-xs text-gray-700 uppercase bg-gray-50">
            <tr>
              <th scope="col" class="px-4 py-3"><div class="w-max flex items-center">标题 </div></th>
              <th scope="col" class="px-4 py-3"><div class="w-max flex items-center">封面 </div></th>
              <th scope="col" class="px-4 py-3"><div class="w-max flex items-center">作者 </div></th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="book in wereadData" class="border-b last:border-none hover:bg-gray-100">
              <td class="px-4 py-4 " v-permission="['plugin:douban:manage']">
                <input
                        v-model="selectBooks"
                        :value="book.metadata.title"
                        class="h-4 w-4 rounded border-gray-300 text-indigo-600"
                        name="post-checkbox"
                        type="checkbox"
                />
              </td>
              <td class="px-4 py-4">{{book.spec.title}}</td>
              <td class="px-4 py-4 poster">
                <img :src="book.spec.cover"  referrerpolicy="no-referrer">
              </td>
              <td class="px-4 py-4">{{book.spec.author}}</td>
            </tr>
            </tbody>
          </table>
        </div>
      </Transition>
      <template #footer>
        <VPagination
                v-model:page="page"
                v-model:size="size"
                :total="total"
                :size-options="[20, 30, 50, 100]"
        />
      </template>
    </VCard>
  </div>
</template>

<script setup lang="ts">
import confetti from "canvas-confetti";
import {onMounted, ref} from "vue";
import { axiosInstance } from "@halo-dev/api-client";
import {
  VCard,
  VPagination,
  VButton,
  VPageHeader,
  VSpace,
  VLoading,
  IconAddCircle,
  Toast,
  Dialog
} from "@halo-dev/components";
import {useQuery} from '@tanstack/vue-query';
onMounted(() => {
  confetti({
    particleCount: 100,
    spread: 70,
    origin: { y: 0.6, x: 0.58 },
  });
});
const selectBooks = ref<string[]>([]);
const page = ref(1);
const size = ref(20);
const total = ref(0);
const keyword = ref(""); // 添加keyword变量
const {
  data: wereadData,
  isLoading,
  isFetching,
  refetch,
} = useQuery({
  queryKey: ["wereadData", page, size, keyword],
  queryFn: async () => {
    const { data } = await axiosInstance.get("/apis/weread.pplay.fun/v1alpha1/books", {
      params: {
        page: page.value,
        size: size.value,
        keyword: keyword.value
      }
    });
    total.value = data.total; // 设置总数
    return data.items;
  }
});
const synchronizationWeRead = () => {
  Dialog.warning({
    title: "同步微信读书数据",
    description: "确定要同步微信读书数据吗，此操作可能会持续较长时间。 ",
    confirmType: "danger",
    confirmText: "确定",
    cancelText: "取消",
    onConfirm: async () => {
      try {
        await axiosInstance.post("/apis/api.plugin.halo.run/v1alpha1/plugins/plugin-weread/weRead/synchronizationWeRead")
                .then((res: any) => {
                  Toast.success("已请求同步微信读书数据");
                });
      } catch (e) {
        console.error("", e);
      }
    },
  });
};
const clearWeRead = ()=>{
  Dialog.warning({
    title: "清空微信读书数据",
    description: "确定要清空微信读书数据吗，此操作不可恢复。 ",
    confirmType: "danger",
    confirmText: "确定",
    cancelText: "取消",
    onConfirm: async () => {
      try {
        await axiosInstance.delete("/apis/api.plugin.halo.run/v1alpha1/plugins/plugin-weread/weRead/clearWeRead")
                .then((res: any) => {
                  Toast.success("清空成功");
                });
      } catch (e) {
        console.error("", e);
      }
    },
  });
}
</script>


<style lang="scss" scoped>
#plugin-starter {
  height: 100vh;
  background-color: #f8fafc;
}

</style>
