//
//  EvaluateTableViewCell.m
//  chengxin
//
//  Created by seniorcoder on 10/30/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "EvaluateTableViewCell.h"
#import "Global.h"

@implementation EvaluateTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (IBAction)onReplyMoreSelected:(id)sender {
    self.moreReplyButton.selected = !self.moreReplyButton.selected;
    [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_MORE_REPLY_VIEW_NOTIFICATION object:nil];
}

- (IBAction)onZanAction:(id)sender {
    
    self.zanButton.selected = !self.zanButton.selected;
    if(self.zanButton.selected)
        self.electCount ++;
    else
        self.electCount --;
    self.electCountLabel.text = [NSString stringWithFormat:@"%ld", (long)self.electCount];
    NSNumber *evaluateMeID = [NSNumber numberWithInteger:self.evaluateID];
    NSNumber *evaluateMe =  self.zanButton.selected? [NSNumber numberWithInteger:1]: [NSNumber numberWithInteger:0];
    NSNumber *evaluateIndex = [NSNumber numberWithInteger:self.cellIndex];
    NSNumber *evaluateTotalValue = [NSNumber numberWithInteger:self.electCount];
    NSMutableArray *evaluateArray = [NSMutableArray array];
    [evaluateArray addObject:evaluateMeID];
    [evaluateArray addObject:evaluateMe];
    [evaluateArray addObject:evaluateIndex];
    [evaluateArray addObject:evaluateTotalValue];
    [[NSNotificationCenter defaultCenter] postNotificationName:UPDATE_EVALUATE_VIEW_NOTIFICATION object:evaluateArray];
}

- (IBAction)onErrorAction:(id)sender {
    int nTestStatus = [[CommonData sharedInstance].userInfo[@"testStatus"] intValue];
    if(nTestStatus != 2) {
        [GeneralUtil showRealnameAuthAlertWithDelegate:self];
    } else {
        NSNumber *evaluateIndex = [NSNumber numberWithInteger:self.cellIndex];
        [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_FIXED_ERROR_VIEW_NOTIFICATION object:evaluateIndex];
    }
}

- (IBAction)onEvaluateAction:(id)sender {
    NSNumber *evaluateIndex = [NSNumber numberWithInteger:self.cellIndex];
    [[NSNotificationCenter defaultCenter] postNotificationName:UPDATE_EVALUATE_DETAIL_VIEW_NOTIFICATION object:evaluateIndex];
}

-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if(buttonIndex == 1)
        [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_REALNAMEAUTH_VIEW_NOTIFICATION object:nil];
}
@end
