//
//  RecordTableViewCell.h
//  chengxin
//
//  Created by common on 7/28/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface RecordTableViewCell : UITableViewCell

@property (nonatomic, weak) IBOutlet UILabel* title;
@property (nonatomic, weak) IBOutlet UILabel* date;
@property (nonatomic, weak) IBOutlet UILabel* rating;
@end
