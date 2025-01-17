// Copyright (C) 2011 The Android Open Source Project
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
// 1. Redistributions of source code must retain the above copyright
//    notice, this list of conditions and the following disclaimer.
// 2. Redistributions in binary form must reproduce the above copyright
//    notice, this list of conditions and the following disclaimer in the
//    documentation and/or other materials provided with the distribution.
// 3. Neither the name of the project nor the names of its contributors
//    may be used to endorse or promote products derived from this software
//    without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE PROJECT AND CONTRIBUTORS ``AS IS'' AND
// ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED.  IN NO EVENT SHALL THE PROJECT OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
// OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
// OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE.
//
// pointer_to_member_type_info.cc: Methods for __pointer_to_member_type_info.

#include <cxxabi.h>

namespace __cxxabiv1
{
  __pointer_to_member_type_info::~__pointer_to_member_type_info()
  {
  }

  bool __pointer_to_member_type_info::do_can_catch_ptr(const __pbase_type_info* thr_type,
                                                       void*& adjustedPtr,
                                                       unsigned nest_const_level,
                                                       bool& result) const {
    const __pointer_to_member_type_info *thrown_type =
        dynamic_cast<const __pointer_to_member_type_info *>(thr_type);
    if (!thrown_type) {
      result = false;
      return true;
    }

    if (*__context != *thrown_type->__context) {
      result = false;     // point to different classes
      return true;
    }

    return false; // Have not decided. Need to recursively call.
  }

} // namespace __cxxabiv1
