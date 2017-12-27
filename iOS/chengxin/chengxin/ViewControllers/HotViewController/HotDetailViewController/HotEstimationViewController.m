//
//  HotEstimationViewController.m
//  chengxin
//
//  Created by common on 5/13/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "HotEstimationViewController.h"
#import "Global.h"

@interface HotEstimationViewController ()

@end

@implementation HotEstimationViewController
{
    BOOL isTextChanged;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    isTextChanged = false;
    if(isTextChanged)
    {
        self.lblCounter.text = [NSString stringWithFormat:@"%lu/200", (unsigned long)self.textView.text.length];
    }else
    {
        self.lblCounter.text = @"0/200";
    }
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [IQKeyboardManager sharedManager].enable = NO;
}
-(void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [IQKeyboardManager sharedManager].enable = YES;
}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/
- (BOOL)textViewShouldBeginEditing:(UITextView *)textView
{
    if(isTextChanged == false)
    {
        self.textView.text = @"";
    }
    return true;
}
- (void)textViewDidChange:(UITextView *)textView
{
    isTextChanged = true;
    self.lblCounter.text = [NSString stringWithFormat:@"%lu/200", (unsigned long)self.textView.text.length];
}
- (BOOL)textView:(UITextView *)textView shouldChangeTextInRange:(NSRange)range replacementText:(NSString *)text
{
    return textView.text.length + (text.length - range.length) <= 200;
}
-(IBAction)onOK:(id)sender
{
    [self.textView resignFirstResponder];
    if(self.textView.text.length == 0 || !isTextChanged)
    {
        //self.textView.text = @"请您发言。";
        [appDelegate.window makeToast:@"评论不能空着哦。"
                    duration:3.0
                    position:CSToastPositionCenter
                       style:nil];
    }else
    {
        [GeneralUtil showProgress];
        NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
        
        [dicParams setObject:@"leaveEstimate" forKey:@"pAct"];
        [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
        [dicParams setObject:@"2" forKey:@"type"];
        [dicParams setObject:[NSNumber numberWithInteger:self.hotData.mId] forKey:@"hotId"];
        [dicParams setObject:self.textView.text forKey:@"content"];
        
        [[WebAPI sharedInstance] sendPostRequestWithUpload:ACTION_LEAVEESTIMATE Parameters:dicParams UploadImages: nil :^(NSObject *resObj) {
            NSDictionary *dicRes = (NSDictionary *)resObj;
            [GeneralUtil hideProgress];
            if (dicRes != nil ) {
                if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                    [self.navigationController popViewControllerAnimated:YES];
                }
                else {
                    [appDelegate.window makeToast:dicRes[@"msg"]
                                duration:3.0
                                position:CSToastPositionCenter
                                   style:nil];
                }
            }else{
                [appDelegate.window makeToast:@"服务器繁忙，请稍后重试"
                            duration:3.0
                            position:CSToastPositionCenter
                               style:nil];
            }
        }];

        
    }
    
}
-(IBAction)onCancel:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}
@end
