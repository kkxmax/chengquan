//
//  HotDetailMainCell.h
//  chengxin
//
//  Created by seniorcoder on 10/27/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HotDetailMainCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UIImageView *imgMainPhoto;
@property (weak, nonatomic) IBOutlet UILabel *lblCount;
@property (weak, nonatomic) IBOutlet UILabel *lblDate;
@property (weak, nonatomic) IBOutlet UILabel *lblTitle;

@end
